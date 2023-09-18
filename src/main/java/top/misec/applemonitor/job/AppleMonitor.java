package top.misec.applemonitor.job;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.config.*;
import top.misec.applemonitor.push.impl.FeiShuBotPush;
import top.misec.applemonitor.push.pojo.feishu.FeiShuPushDTO;
import top.misec.bark.BarkPush;
import top.misec.bark.enums.SoundEnum;
import top.misec.bark.pojo.PushDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author MoshiCoCo
 */
@Slf4j
public class AppleMonitor {
    private final AppCfg CONFIG = CfgSingleton.getInstance().config;


    public void monitor() {

        List<DeviceItem> deviceItemList = CONFIG.getAppleTaskConfig().getDeviceCodeList();
        //监视机型型号

        try {
            for (DeviceItem deviceItem : deviceItemList) {
                doMonitor(deviceItem);
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            log.error("AppleMonitor Error", e);
        }
    }


    public void pushAll(String content, List<PushConfig> pushConfigs) {

        pushConfigs.forEach(push -> {

            if (StrUtil.isAllNotEmpty(push.getBarkPushUrl(), push.getBarkPushToken())) {
                BarkPush barkPush= BarkPush.builder()
                        .pushUrl(push.getBarkPushUrl())
                        .deviceKey(push.getBarkPushToken())
                        .build();
                PushDetails pushDetails= PushDetails.builder()
                        .title("苹果商店监控")
                        .body(content)
                        .category("苹果商店监控")
                        .group("Apple Monitor")
                        .sound(SoundEnum.GLASS.getSoundName())
                        .build();
                barkPush.simpleWithResp(pushDetails);
            }
            if (StrUtil.isAllNotEmpty(push.getFeishuBotSecret(), push.getFeishuBotWebhooks())) {

                FeiShuBotPush.pushTextMessage(FeiShuPushDTO.builder()
                        .text(content).secret(push.getFeishuBotSecret())
                        .botWebHooks(push.getFeishuBotWebhooks())
                        .build());
            }
        });

    }

    public void doMonitor(DeviceItem deviceItem) {

        Map<String, Object> queryMap = new HashMap<>(5);
        queryMap.put("pl", "true");
        queryMap.put("mts.0", "regular");
        queryMap.put("parts.0", deviceItem.getDeviceCode());
        queryMap.put("location", CONFIG.getAppleTaskConfig().getLocation());

        String baseCountryUrl = CountryEnum.getUrlByCountry(CONFIG.getAppleTaskConfig().getCountry());

        Map<String, List<String>> headers = buildHeaders(baseCountryUrl, deviceItem.getDeviceCode());

        String url = baseCountryUrl + "/shop/fulfillment-messages?" + URLUtil.buildQuery(queryMap, CharsetUtil.CHARSET_UTF_8);

        try {
            HttpResponse httpResponse = HttpRequest.get(url).header(headers).execute();
            if (!httpResponse.isOk()) {
                log.info("请求过于频繁，请调整cronExpressions，建议您参考推荐的cron表达式");
                return;
            }

            JSONObject responseJsonObject = JSONObject.parseObject(httpResponse.body());

            JSONObject pickupMessage = responseJsonObject.getJSONObject("body").getJSONObject("content").getJSONObject("pickupMessage");

            JSONArray stores = pickupMessage.getJSONArray("stores");

            if (stores == null) {
                log.info("您可能填错产品代码了，目前仅支持监控中国和日本地区的产品，注意不同国家的机型型号不同，下面是是错误信息");
                log.debug(pickupMessage.toString());
                return;
            }

            if (stores.isEmpty()) {
                log.info("您所在的 {} 附近没有Apple直营店，请检查您的地址是否正确", CONFIG.getAppleTaskConfig().getLocation());
            }

            stores.stream().filter(store -> {
                if (deviceItem.getStoreWhiteList().isEmpty()) {
                    return true;
                } else {
                    return filterStore((JSONObject) store, deviceItem);
                }
            }).forEach(k -> {

                JSONObject storeJson = (JSONObject) k;

                JSONObject partsAvailability = storeJson.getJSONObject("partsAvailability");

                String storeNames = storeJson.getString("storeName").trim();
                String deviceName = partsAvailability.getJSONObject(deviceItem.getDeviceCode()).getJSONObject("messageTypes").getJSONObject("regular").getString("storePickupProductTitle");
                String productStatus = partsAvailability.getJSONObject(deviceItem.getDeviceCode()).getString("pickupSearchQuote");


                String strTemp = "门店:{},型号:{},状态:{}";

                String content = StrUtil.format(strTemp, storeNames, deviceName, productStatus);

                if (judgingStoreInventory(storeJson, deviceItem.getDeviceCode())) {
                    JSONObject retailStore = storeJson.getJSONObject("retailStore");
                    content += buildPickupInformation(retailStore);
                    log.info(content);

                    pushAll(content, deviceItem.getPushConfigs());


                }
                log.info(content);
            });

        } catch (Exception e) {
            log.error("AppleMonitor error", e);
        }

    }


    /**
     * check store inventory
     *
     * @param storeJson   store json
     * @param productCode product code
     * @return boolean
     */
    private boolean judgingStoreInventory(JSONObject storeJson, String productCode) {

        JSONObject partsAvailability = storeJson.getJSONObject("partsAvailability");
        String status = partsAvailability.getJSONObject(productCode).getString("pickupDisplay");
        return "available".equals(status);

    }

    /**
     * build pickup information
     *
     * @param retailStore retailStore
     * @return pickup message
     */
    private String buildPickupInformation(JSONObject retailStore) {
        String distanceWithUnit = retailStore.getString("distanceWithUnit");
        String twoLineAddress = retailStore.getJSONObject("address").getString("twoLineAddress");
        String daytimePhone = retailStore.getJSONObject("address").getString("daytimePhone");
        String lo = CONFIG.getAppleTaskConfig().getLocation();
        String messageTemplate = "\n取货地址:{},电话:{},距离{}:{}";
        return StrUtil.format(messageTemplate, twoLineAddress.replace("\n", " "), daytimePhone, lo, distanceWithUnit);
    }

    private boolean filterStore(JSONObject storeInfo, DeviceItem deviceItem) {
        String storeName = storeInfo.getString("storeName");
        return deviceItem.getStoreWhiteList().stream().anyMatch(k -> storeName.contains(k) || k.contains(storeName));
    }

    /**
     * build request headers
     *
     * @param baseCountryUrl base country url
     * @param productCode    product code
     * @return headers
     */
    private Map<String, List<String>> buildHeaders(String baseCountryUrl, String productCode) {

        ArrayList<String> referer = new ArrayList<>();
        referer.add(baseCountryUrl + "/shop/buy-iphone/iphone-14-pro/" + productCode);

        Map<String, List<String>> headers = new HashMap<>(10);
        headers.put(Header.REFERER.getValue(), referer);

        return headers;
    }
}
