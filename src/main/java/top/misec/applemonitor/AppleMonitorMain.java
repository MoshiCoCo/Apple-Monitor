package top.misec.applemonitor;

import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;

/**
 * @author moshi
 */


public class AppleMonitorMain {

    public static void main(String[] args) {

        AppCfg config = CfgSingleton.getInstance().config;

        if (config.getAppleTaskConfig().valid()) {
            Setting setting = new Setting();
            setting.set("top.misec.applemonitor.job.AppleMonitor.monitor", config.getAppleTaskConfig().getCronExpressions());

            CronUtil.setCronSetting(setting);
            CronUtil.setMatchSecond(true);

            CronUtil.start(true);
            while (true) {

            }
        }

    }

}
