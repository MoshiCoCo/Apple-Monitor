package top.misec.applemonitor;


import org.junit.jupiter.api.Test;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;
import top.misec.applemonitor.push.impl.BarkPush;

class AppleMonitorApplicationTest {

    @Test
    void contextLoads() {

    }

    @Test
    void pushTest() {
        AppCfg config = getAppCfg();

        BarkPush.push("test", config.getPushConfig().getBarkPushUrl(), config.getPushConfig().getBarkPushToken());

    }

    private AppCfg getAppCfg() {
        return CfgSingleton.getInstance().config;
    }


}
