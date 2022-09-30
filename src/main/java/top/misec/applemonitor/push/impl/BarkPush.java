package top.misec.applemonitor.push.impl;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;

import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.push.BarkPushBody;

/**
 * @author moshi
 */
@Slf4j
public class BarkPush {

    public static void push(String content, String pushApi, String pushToken) {

        Map<String, Object> ext = new HashMap<>(0xa);

        ext.put("icon", "https://image-pics.oss-cn-hangzhou.aliyuncs.com/1.jpg");


        BarkPushBody barkPushBody = new BarkPushBody();

        barkPushBody.setDeviceKey(pushToken);
        barkPushBody.setBody(content);
        barkPushBody.setTitle("苹果商店监控");
        barkPushBody.setCategory("苹果商店监控");
        barkPushBody.setExtParams(JSON.toJSONString(ext));


        String res = HttpRequest.post(pushApi)
                .header(Header.CONTENT_TYPE, "application/json; charset=utf-8")
                .body(JSON.toJSONString(barkPushBody)).execute().body();

        JSONObject result = JSON.parseObject(res);

        if (result.getInteger("code") == HttpStatus.HTTP_OK) {
            log.info("监控状态推送成功");
        }
    }
}
