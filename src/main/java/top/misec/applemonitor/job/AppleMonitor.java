package top.misec.applemonitor.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;
import top.misec.applemonitor.config.CountryEnum;
import top.misec.applemonitor.push.impl.BarkPush;

/**
 * @author Moshi
 */
@Slf4j
public class AppleMonitor {
    private final AppCfg CONFIG = CfgSingleton.getInstance().config;

    public void monitor() {

        //监视机型型号
        List<String> productList = CONFIG.getAppleTaskConfig().getDeviceCodes();

        try {
            for (String k : productList) {
                doMonitor(k);
                Thread.sleep(2500);
            }
        } catch (Exception e) {
            log.error("AppleMonitor error", e);
        }
    }

    public void doMonitor(String productCode) {

        String baseCountryURL = CountryEnum.getUrlByCountry(CONFIG.getAppleTaskConfig().getCountry());

        Map<String, Object> queryMap = new HashMap<>(5);
        queryMap.put("pl", "true");
        queryMap.put("true", "true");
        queryMap.put("mts.0", "regular");
        queryMap.put("parts.0", productCode);
        queryMap.put("location", CONFIG.getAppleTaskConfig().getLocation());


        Map<String, List<String>> headers = new HashMap<>(10);
        ArrayList<String> referer = new ArrayList<>();

        referer.add(baseCountryURL + "/shop/buy-iphone/iphone-14-pro/" + productCode);

        headers.put(Header.REFERER.getValue(), referer);

        String url = baseCountryURL + "/shop/fulfillment-messages?"
                + URLUtil.buildQuery(queryMap, CharsetUtil.CHARSET_UTF_8);

        try {
            HttpResponse httpResponse = HttpRequest.get(url).header(headers).execute();
            if (!httpResponse.isOk()) {
                log.info("请求过于频繁，请调整cronExpressions，建议您参考推荐的cron表达式");
                return;
            }

            JSONObject responseJsonObject = JSONObject.parseObject(httpResponse.body());

            JSONObject pickupMessage = responseJsonObject.getJSONObject("body")
                    .getJSONObject("content")
                    .getJSONObject("pickupMessage");

            JSONArray stores = pickupMessage.getJSONArray("stores");

            if (stores == null || stores.isEmpty()) {
                log.info("您可能填错产品代码了，目前仅支持监控中国和日本地区的产品，注意不同国家的机型型号不同，下面是是错误信息");
                log.debug(pickupMessage.toString());
                return;
            }

            stores.stream().filter(store -> {
                if (CONFIG.getAppleTaskConfig().getStoreWhiteList().isEmpty()) {
                    return true;
                } else {
                    return filterStore((JSONObject) store);
                }
            }).forEach(k -> {

                JSONObject storeJson = (JSONObject) k;

                JSONObject partsAvailability = storeJson.getJSONObject("partsAvailability");

                String storeNames = storeJson.getString("storeName").trim();
                String deviceName = partsAvailability.getJSONObject(productCode)
                        .getJSONObject("messageTypes")
                        .getJSONObject("regular")
                        .getString("storePickupProductTitle");
                String content = storeNames + " - " + deviceName + " - " + partsAvailability.getJSONObject(productCode).getString("pickupSearchQuote");

                if (judgingStoreInventory(storeJson, productCode)) {
                    JSONObject retailStore = storeJson.getJSONObject("retailStore");
                    content += buildPickupInformation(retailStore);
                    log.info(content);
                    BarkPush.push(content, CONFIG.getPushConfig().getBarkPushUrl(), CONFIG.getPushConfig().barkPushToken);
                    BarkPush.push(content, CONFIG.getPushConfig().getBarkPushUrl(), "hxeGD2F5Gr2WM6q5Me89aG");
                }

                log.info(content);
            });

        } catch (Exception e) {
            log.error("AppleMonitor error", e);
        }

    }

    private boolean judgingStoreInventory(JSONObject storeJson, String productCode) {

        JSONObject partsAvailability = storeJson.getJSONObject("partsAvailability");
        String status = partsAvailability.getJSONObject(productCode).getString("pickupDisplay");
        return "available".equals(status);

    }

    private String buildPickupInformation(JSONObject retailStore) {
        String distanceWithUnit = retailStore.getString("distanceWithUnit");
        String twoLineAddress = retailStore.getJSONObject("address").getString("twoLineAddress");
        String daytimePhone = retailStore.getJSONObject("address").getString("daytimePhone");
        String lo = CONFIG.getAppleTaskConfig().getLocation();

        String messageTemplate = " 取货地址: {},电话: {},距离{} :{}";
        return StrUtil.format(messageTemplate, twoLineAddress.replace("\n", " "), daytimePhone, lo, distanceWithUnit);
    }

    private boolean filterStore(JSONObject storeInfo) {
        String storeName = storeInfo.getString("storeName");
        return CONFIG.getAppleTaskConfig().getStoreWhiteList().stream().anyMatch(k -> storeName.contains(k) || k.contains(storeName));
    }
}
