package top.misec.applemonitor.push.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.push.pojo.feishu.FeiShuPushDTO;
import top.misec.applemonitor.push.pojo.feishu.FeiShuPushReq;
import top.misec.applemonitor.push.pojo.feishu.TextContent;
import top.misec.applemonitor.utils.FeiShuUtils;

/**
 * 飞书机器人
 *
 * @author Moshi
 * @since 2023/5/10
 */
@Slf4j
public class FeiShuBotPush {

    /**
     * 推送文本消息
     *
     * @param feiShuPushDTO feiShuPushDTO
     */

    public static void pushTextMessage(FeiShuPushDTO feiShuPushDTO) {
        long timestamp = System.currentTimeMillis() / 1000;
        try (HttpResponse httpResponse = HttpRequest.post(feiShuPushDTO.getBotWebHooks())
                .body(JSONObject.toJSONString(FeiShuPushReq.builder()
                        .content(TextContent.builder().text(feiShuPushDTO.getText()).build())
                        .timestamp(timestamp)
                        .msgType("text")
                        .sign(FeiShuUtils.genSign(feiShuPushDTO.getSecret(), timestamp))
                        .build()))
                .execute()) {
            log.info("飞书机器人推送状态:{}", httpResponse.getStatus());
            log.info(httpResponse.body());
        }
    }
}
