package top.misec.applemonitor;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;
import top.misec.applemonitor.job.AppleMonitor;
import top.misec.applemonitor.push.impl.BarkPush;

@Slf4j
class AppleMonitorApplicationTest {

    @Test
    void contextLoads() {

    }

    @Test
    void pushTest() {
        AppCfg config = getAppCfg();

        BarkPush.push("test", config.getPushConfig().getBarkPushUrl(), config.getPushConfig().getBarkPushToken());

    }

    @Test
    void monitorTest() {
        AppCfg config = getAppCfg();

        log.info("config: {}", config);

        AppleMonitor appleMonitor = new AppleMonitor();
        appleMonitor.monitor();
    }

    private AppCfg getAppCfg() {
        return CfgSingleton.getInstance().config;
    }


}
