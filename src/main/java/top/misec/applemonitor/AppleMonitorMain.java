package top.misec.applemonitor;

import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author moshi
 */

@Slf4j
public class AppleMonitorMain {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Condition STOP = LOCK.newCondition();

    public static void main(String[] args) {

        AppCfg appCfg = CfgSingleton.getInstance().config;

        if (appCfg.getAppleTaskConfig().valid()) {
            Setting setting = new Setting();
            setting.set("top.misec.applemonitor.job.AppleMonitor.monitor", appCfg.getAppleTaskConfig().getCronExpressions());

            CronUtil.setCronSetting(setting);
            CronUtil.setMatchSecond(true);
            CronUtil.start(true);

            LOCK.lock();
            try {
                STOP.await();
            } catch (InterruptedException e) {
                log.info("AppleMonitorMain is interrupted");
            } finally {
                LOCK.unlock();
            }
        }


    }

}
