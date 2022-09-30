package top.misec.applemonitor.push;

import com.alibaba.fastjson2.annotation.JSONField;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author moshi
 */
@Data
@Accessors
public class BarkPushBody {
    String body;
    String title;
    String category;
    @JSONField(name = "device_key")
    String deviceKey;
    @JSONField(name = "ext_params")
    String extParams;
}
