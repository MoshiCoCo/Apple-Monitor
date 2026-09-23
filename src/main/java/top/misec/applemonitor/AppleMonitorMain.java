package top.misec.applemonitor;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import cn.hutool.core.util.StrUtil;
import cn.hutool.cron.CronUtil;
import cn.hutool.http.HttpGlobalConfig;
import cn.hutool.setting.Setting;
import lombok.extern.slf4j.Slf4j;
import top.misec.applemonitor.config.AppCfg;
import top.misec.applemonitor.config.AppleTaskConfig;
import top.misec.applemonitor.config.CfgSingleton;

/**
 * @author moshi
 */

@Slf4j
public class AppleMonitorMain {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static final Condition STOP = LOCK.newCondition();

    /**
     * hutool 默认不设超时，一次挂起的请求会永久占死 cron 线程，监控静默停摆。
     * 这里走全局配置而非逐个请求设置，因为 bark-java-sdk 内部自建 HttpRequest，无法从外部注入。
     */
    private static final int HTTP_TIMEOUT_MS = 10_000;

    public static void main(String[] args) {

        HttpGlobalConfig.setTimeout(HTTP_TIMEOUT_MS);

        AppCfg appCfg = CfgSingleton.getInstance().config;
        AppleTaskConfig taskConfig = appCfg == null ? null : appCfg.getAppleTaskConfig();

        if (taskConfig == null) {
            log.error("未能读取配置，请确认程序运行目录下存在 config.json 且格式正确");
            System.exit(1);
        }

        if (!taskConfig.valid()) {
            log.error("配置校验未通过，请修正 config.json 后重新启动");
            System.exit(1);
        }

        int size = taskConfig.deviceCodeList.size();

        String cronExpress = StrUtil.format("*/{} * * * * ?", size * 3);

        log.info("您本次共监控{}个机型，过短的执行时间间隔会导致请求被限制，建议您的cron表达式设置为:{}", size, cronExpress);

        Setting setting = new Setting();
        setting.set("top.misec.applemonitor.job.AppleMonitor.monitor", taskConfig.cronExpressions);


        CronUtil.setCronSetting(setting);
        CronUtil.setMatchSecond(true);
        CronUtil.start(true);

        LOCK.lock();
        try {
            STOP.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("AppleMonitorMain is interrupted");
        } finally {
            LOCK.unlock();
        }


    }

}
