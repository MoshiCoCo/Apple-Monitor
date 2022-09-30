package top.misec.applemonitor.config;

import java.io.File;

import com.alibaba.fastjson2.JSONObject;

import top.misec.applemonitor.utils.FileReader;

/**
 * @author moshi
 */
public class CfgSingleton {

    public AppCfg config;

    private volatile static CfgSingleton uniqueInstance;

    private CfgSingleton() {
        String currentPath = System.getProperty("user.dir") + File.separator + "config.json";
        String configStr = FileReader.readFile(currentPath);

        this.config = JSONObject.parseObject(configStr, AppCfg.class);

    }

    public static CfgSingleton getInstance() {

        if (uniqueInstance == null) {
            synchronized (CfgSingleton.class) {
                if (uniqueInstance == null) {
                    uniqueInstance = new CfgSingleton();
                }
            }
        }
        return uniqueInstance;
    }


}
