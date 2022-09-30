package top.misec.applemonitor.push.impl;

import com.alibaba.fastjson2.JSONObject;

import top.misec.applemonitor.config.PushApi;
import top.misec.applemonitor.push.AbstractPush;
import top.misec.applemonitor.push.model.PushMetaInfo;

/**
 * TG推送 .
 *
 * @author itning
 * @since 2021/3/22 17:55
 */
public class TelegramPush extends AbstractPush {

    @Override
    protected String generatePushUrl(PushMetaInfo metaInfo) {
        return PushApi.SERVER_PUSH_TELEGRAM + metaInfo.getToken() + "/sendMessage";
    }

    @Override
    protected boolean checkPushStatus(JSONObject jsonObject) {
        if (null == jsonObject) {
            return false;
        }

        String ok = jsonObject.getString("ok");
        if (null == ok) {
            return false;
        }

        return "true".equals(ok);
    }

    @Override
    protected String generatePushBody(PushMetaInfo metaInfo, String content) {
        return "chat_id=" + metaInfo.getChatId() + "&text=任务简报\n" + content;
    }
}
