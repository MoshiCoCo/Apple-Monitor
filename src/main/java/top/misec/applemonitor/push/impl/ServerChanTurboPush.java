package top.misec.applemonitor.push.impl;

import com.alibaba.fastjson2.JSONObject;

import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.config.PushApi;
import top.misec.applemonitor.push.AbstractPush;
import top.misec.applemonitor.push.model.PushMetaInfo;

/**
 * Turbo版本server酱推送.
 *
 * @author itning
 * @since 2021/3/22 17:14
 */
@Slf4j
public class ServerChanTurboPush extends AbstractPush {

    @Override
    protected String generatePushUrl(PushMetaInfo metaInfo) {
        return PushApi.SERVER_PUSH_V2 + metaInfo.getToken() + ".send";
    }

    @Override
    protected boolean checkPushStatus(JSONObject jsonObject) {
        if (null == jsonObject) {
            return false;
        }
        // {"code":0,"message":"","data":{"pushid":"XXX","readkey":"XXX","error":"SUCCESS","errno":0}}
        Integer code = jsonObject.getInteger("code");

        if (null == code) {
            return false;
        }

        // FIX #380
        switch (code) {
            case 0:
                return true;
            case 40001:
                log.info("超过当天的发送次数限制[10]，请稍后再试");
                return true;
            default:
                return code == 0;
        }
    }

    @Override
    protected String generatePushBody(PushMetaInfo metaInfo, String content) {
        return "title=任务简报&desp=" + content.replaceAll("=", ":");
    }
}
