package top.misec.applemonitor.push.impl;

import com.alibaba.fastjson2.JSONObject;

import lombok.Getter;
import top.misec.applemonitor.config.PushApi;
import top.misec.applemonitor.push.AbstractPush;
import top.misec.applemonitor.push.model.PushMetaInfo;

/**
 * WeiXinPush .
 *
 * @author itning
 * @since 2021-05-06 18:10
 **/
public class WeComPush extends AbstractPush {

    @Override
    protected String generatePushUrl(PushMetaInfo metaInfo) {
        return PushApi.WECHAT_PUSH + metaInfo.getToken();
    }

    @Override
    protected boolean checkPushStatus(JSONObject jsonObject) {
        if (jsonObject == null) {
            return false;
        }
        Integer errcode = jsonObject.getInteger("errcode");
        String errmsg = jsonObject.getString("errmsg");
        if (null == errcode || null == errmsg) {
            return false;
        }
        return errcode == 0 && "ok".equals(errmsg);
    }

    @Override
    protected String generatePushBody(PushMetaInfo metaInfo, String content) {

        return JSONObject.toJSONString(content);
//        return new Gson().toJson(new MessageModel(content));
    }

    @Getter
    static class MessageModel {
        private final String msgtype = "markdown";
        private final Markdown markdown;

        public MessageModel(String content) {
            this.markdown = new Markdown(content);
        }
    }

    @Getter
    static class Markdown {
        private final String content;

        public Markdown(String content) {
            this.content = content.replaceAll("\r\n\r", "");
        }
    }
}
