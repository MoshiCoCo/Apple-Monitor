package top.misec.applemonitor;

import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import top.misec.applemonitor.config.MonitorCfg;
import top.misec.applemonitor.config.CfgSingleton;

/**
 * @author junzhou
 */


public class AppleMonitorMain {
    
    public static void main(String[] args) {
        
        MonitorCfg config = CfgSingleton.getInstance().config;
        
        if (config.valid()) {
            Setting setting = new Setting();
            setting.set("top.misec.applemonitor.job.AppleMonitor.monitor", config.getCronExpressions());
            
            CronUtil.setCronSetting(setting);
            CronUtil.setMatchSecond(true);
            
            CronUtil.start(true);
            while (true) {
            
            }
        }
        
    }
    
}
