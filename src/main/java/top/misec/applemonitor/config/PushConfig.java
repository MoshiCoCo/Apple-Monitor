package top.misec.applemonitor.config;

import lombok.Data;


/**
 * push config ddd .
 *
 * @author msohi itning
 */
@SuppressWarnings("all")
@Data
public class PushConfig {

    /**
     * barkPushUrl
     */
    public String barkPushUrl;
    /**
     * barkPushToken
     */
    public String barkPushToken;

    public String barkPushSound;

    public String feishuBotWebhooks;
    public String feishuBotSecret;
}
