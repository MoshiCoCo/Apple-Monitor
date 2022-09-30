package top.misec.applemonitor.push.impl;

import com.alibaba.fastjson2.JSONObject;

import top.misec.applemonitor.config.PushApi;
import top.misec.applemonitor.push.AbstractPush;
import top.misec.applemonitor.push.model.PushMetaInfo;

/**
 * server酱推送 .
 * Server酱旧版推送渠道即将下线，使用Turbo版本{@link ServerChanTurboPush}替代 .
 *
 * @author itning
 * @since 2021/3/22 16:37
 */
public class ServerChanPush extends AbstractPush {

    @Override
    protected String generatePushUrl(PushMetaInfo metaInfo) {
        return PushApi.SERVER_PUSH + metaInfo.getToken() + ".send";
    }

    @Override
    protected boolean checkPushStatus(JSONObject jsonObject) {
        if (null == jsonObject) {
            return false;
        }

        Integer code = jsonObject.getInteger("code");
        Integer errno = jsonObject.getInteger("errno");

        if (null != code && code == 0) {
            return true;
        }

        if (null != errno && errno == 0) {
            return true;
        }

        return false;
    }

    @Override
    protected String generatePushBody(PushMetaInfo metaInfo, String content) {
        return "text=任务简报&desp=" + content.replaceAll("=", ":");
    }
}