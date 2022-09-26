package top.misec.applemonitor.push;

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
    String device_key;
    String ext_params;
}
