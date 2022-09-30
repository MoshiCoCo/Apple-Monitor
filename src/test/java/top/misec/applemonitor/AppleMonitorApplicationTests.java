package top.misec.applemonitor;


import org.junit.jupiter.api.Test;

import com.alibaba.fastjson2.JSON;

import top.misec.applemonitor.config.CfgSingleton;
import top.misec.applemonitor.config.AppCfg;

class AppleMonitorApplicationTests {

    @Test
    void contextLoads() {
        AppCfg config = CfgSingleton.getInstance().config;

        System.out.println(JSON.toJSONString(config));
    }

}
