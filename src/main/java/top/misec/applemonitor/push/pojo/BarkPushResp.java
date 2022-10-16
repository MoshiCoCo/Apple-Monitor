package top.misec.applemonitor.push.pojo;

import lombok.Data;

/**
 * @author moshi
 */
@Data
public class BarkPushResp {

    private Integer code;
    private String message;
    private Integer timestamp;
}
