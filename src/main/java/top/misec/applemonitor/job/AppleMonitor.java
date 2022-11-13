package top.misec.applemonitor.job;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;
import top.misec.applemonitor.config.CountryEnum;
import top.misec.applemonitor.push.impl.BarkPush;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                doMonitor(CONFIG.getAppleTaskConfig().getLocation(), k);
            }
        } catch (Exception e) {
            log.error("AppleMonitor error", e);
        }
    }

    public void doMonitor(String locationName, String productCode) {

        Map<String, Object> queryMap = new HashMap<>(10);
        queryMap.put("pl", "true");
        queryMap.put("mts.0", "regular");
        queryMap.put("parts.0", productCode);
        queryMap.put("location", locationName);


        String baseUrl = CountryEnum.getUrlByCountry(CONFIG.getAppleTaskConfig().getCountry());

        String url = baseUrl + "/shop/fulfillment-messages?" + URLUtil.buildQuery(queryMap, CharsetUtil.CHARSET_UTF_8);


        try {

            HttpResponse httpResponse = HttpRequest.get(url).execute();

            if (!httpResponse.isOk()) {
                log.info("正在持续监控中...");
                return;
            }

            JSONObject responseJsonObject = JSONObject.parseObject(httpResponse.body());

            if ("200".equals(responseJsonObject.getJSONObject("head").get("status"))) {

                JSONObject pickupMessage = responseJsonObject.getJSONObject("body")
                        .getJSONObject("content")
                        .getJSONObject("pickupMessage");

                JSONArray stores = pickupMessage.getJSONArray("stores");

                if (stores == null) {
                    log.info("您可能填错产品代码了，目前仅支持监控中国大陆和日本地区的产品，注意不同国家的机型型号不同，下面是是错误信息");
                    log.info(pickupMessage.toString());
                    return;
                }

                stores.stream().filter(store -> {

                    if (CONFIG.getAppleTaskConfig().getStoreWhiteList().isEmpty()) {
                        return true;
                    }

                    JSONObject storeInfo = (JSONObject) store;

                    String storeName = storeInfo.getString("storeName");

                    return CONFIG.getAppleTaskConfig().getStoreWhiteList().stream().anyMatch(k -> storeName.contains(k) || k.contains(storeName));

                }).forEach(k -> {

                    JSONObject storeJson = (JSONObject) k;

                    JSONObject partsAvailability = storeJson.getJSONObject("partsAvailability");

                    String storeNames = storeJson.getString("storeName").trim();


                    String deviceName = partsAvailability.getJSONObject(productCode)
                            .getJSONObject("messageTypes")
                            .getJSONObject("regular")
                            .getString("storePickupProductTitle");


                    String status = partsAvailability.getJSONObject(productCode).getString("pickupDisplay");

                    String content = storeNames + " - " + deviceName + " - " + partsAvailability.getJSONObject(productCode).getString("pickupSearchQuote");

                    if ("available".equals(status)) {
                        BarkPush.push(content, CONFIG.getPushConfig().getBarkPushUrl(), CONFIG.getPushConfig().barkPushToken);
                    }

                    log.info(content);

                });
            }
        } catch (Exception e) {
            log.error("AppleMonitor error", e);
        }

    }
}
