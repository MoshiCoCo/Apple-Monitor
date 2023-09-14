package top.misec.applemonitor.push.pojo.feishu;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Builder;
import lombok.Data;

/**
 * FeiShuPushReq
 *
 * @author Moshi
 * @since 2023/5/10
 */
@Data
@Builder
public class FeiShuPushReq {

    @JSONField(name = "msg_type")
    private String msgType;

    private TextContent content;

    private long timestamp;

    private String sign;

}
