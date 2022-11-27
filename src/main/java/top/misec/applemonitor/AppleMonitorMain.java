package top.misec.applemonitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.CfgSingleton;

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

            int size = appCfg.getAppleTaskConfig().deviceCodes.size();

            String cronExpress = StrUtil.format("*/{} * * * * ?", size * 3);
            log.info("您本次共监控{}个机型，过短的执行时间间隔会导致请求被限制，建议您的cron表达式设置为:{}", size, cronExpress);

            Setting setting = new Setting();
            setting.set("top.misec.applemonitor.job.AppleMonitor.monitor", appCfg.getAppleTaskConfig().cronExpressions);


            CronUtil.setCronSetting(setting);
            CronUtil.setMatchSecond(true);
            CronUtil.start(true);
        }

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
