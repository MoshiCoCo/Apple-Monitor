package top.misec.applemonitor.push.pojo;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author moshi
 */
@Data
@Accessors
public class BarkPushBody {
    private String body;
    private String title;
    private String category;
    @JSONField(name = "device_key")
    private String deviceKey;
    private String badge;
    private String sound;
    private String icon;
    private String group;
    private String url;
}
