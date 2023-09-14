package top.misec.applemonitor;

import org.junit.jupiter.api.Test;

import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;
import top.misec.applemonitor.job.AppleMonitor;


@Slf4j
class AppleMonitorApplicationTest {

    @Test
    void contextLoads() {

    }

    @Test
    void pushTest() {
        String jpCfg = "config-jp.json";
        AppCfg config = getAppCfg(jpCfg);
        new AppleMonitor().monitor();
    }

    @Test
    void monitorTest() {
        String jpCfg = "config-jp.json";
        AppCfg config = getAppCfg(jpCfg);
        log.info("config: {}", config);
        new AppleMonitor().monitor();
    }

    @Test
    void monitorTestCN() {
        String jpCfg = "config.json";
        AppCfg config = getAppCfg(jpCfg);
        log.info("config: {}", config);
        new AppleMonitor().monitor();
    }


    private AppCfg getAppCfg(String fileName) {
        return CfgSingleton.getTestInstance(fileName).config;
    }


}
