package top.misec.applemonitor.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Moshi
 */
@Data
@Slf4j
public class AppCfg {

    private AppleTaskConfig appleTaskConfig;

    private PushConfig pushConfig;

}
