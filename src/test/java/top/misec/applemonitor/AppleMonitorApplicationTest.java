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
        String jpCfg = "config-jp.json";
        AppCfg config = getAppCfg(jpCfg);

        BarkPush.push("test", config.getPushConfig().getBarkPushUrl(), config.getPushConfig().getBarkPushToken());

    }

    @Test
    void monitorTest() {
        String jpCfg = "config-jp.json";
        AppCfg config = getAppCfg(jpCfg);

        log.info("config: {}", config);

        AppleMonitor appleMonitor = new AppleMonitor();
        appleMonitor.monitor();
    }


    private AppCfg getAppCfg(String fileName) {
        return CfgSingleton.getTestInstance(fileName).config;
    }


}
