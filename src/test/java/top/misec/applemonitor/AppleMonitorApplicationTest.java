package top.misec.applemonitor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;
import top.misec.applemonitor.config.PushConfig;
import top.misec.applemonitor.job.AppleMonitor;
import top.misec.bark.BarkPush;
import top.misec.bark.enums.SoundEnum;
import top.misec.bark.pojo.PushDetails;


@Slf4j
class AppleMonitorApplicationTest {

    @Test
    void contextLoads() {

    }

    @Test
    void test() {
        String jpCfg = "config-test.json";
        AppCfg config = getAppCfg(jpCfg);

        System.out.println(config);

        PushConfig pushConfig = config.getAppleTaskConfig().getDeviceCodeList().get(0).getPushConfigs().get(0);

        BarkPush barkPush = new BarkPush(pushConfig.getBarkPushUrl(), pushConfig.getBarkPushToken());
        PushDetails pushDetails = PushDetails.builder()
                .title("苹果商店监控")
                .body("123")
                .category("苹果商店监控")
                .group("Apple Monitor")
                .sound(SoundEnum.MULTIWAYINVITATION.getSoundName())
                .build();
        barkPush.simpleWithResp(pushDetails);
        log.info("read config : {}", config);
    }

    @Test
    void pushTest() {
        String jpCfg = "config-jp.json";
        AppCfg config = getAppCfg(jpCfg);
        new AppleMonitor().monitor();
    }

    @Test
    void monitorLocal() {
        String jpCfg = "config-test.json";
        AppCfg config = getAppCfg(jpCfg);
        log.info("monitor local config: {}", config);
        new AppleMonitor().monitor();
    }

    @Test
    void monitorTest() {
        String jpCfg = "config-jp.json";
        AppCfg config = getAppCfg(jpCfg);
        log.info("config jp: {}", config);
        new AppleMonitor().monitor();
    }

    @Test
    void monitorTestCN() {
        String cnCfg = "config.json";
        AppCfg config = getAppCfg(cnCfg);
        log.info("config: {}", config);
        new AppleMonitor().monitor();
    }

    @Test
    void monitorTestKR() {
        String krCfg = "config-kr.json";
        AppCfg config = getAppCfg(krCfg);
        log.info("config: {}", config);
        new AppleMonitor().monitor();
    }


    private AppCfg getAppCfg(String fileName) {
        return CfgSingleton.getTestInstance(fileName).config;
    }

}
