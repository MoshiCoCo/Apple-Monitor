package top.misec.applemonitor.push.impl;

import com.alibaba.fastjson2.JSON;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpStatus;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.push.pojo.BarkPushBody;
import top.misec.applemonitor.push.pojo.BarkPushResp;

/**
 * @author moshi
 */
@Slf4j
public class BarkPush {

    public static void push(String content, String pushApi, String pushToken) {

        HttpResponse httpResponse = HttpRequest.post(pushApi)
                .body(JSON.toJSONString(BarkPushBody.builder()
                        .deviceKey(pushToken)
                        .body(content)
                        .title("苹果商店监控")
                        .category("苹果商店监控")
                        .group("Apple Monitor")
                        .build()))
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .execute();


        if (httpResponse.isOk()) {
            BarkPushResp barkPushResp = JSON.parseObject(httpResponse.body(), BarkPushResp.class);
            if (barkPushResp.getCode() == HttpStatus.HTTP_OK) {
                log.info("监控状态推送成功");
            }
        } else {
            log.info("推送服务器异常 : {} ", httpResponse);
        }

    }
}
