package top.misec.applemonitor.config;

import com.alibaba.fastjson2.JSONObject;
import top.misec.applemonitor.utils.FileReader;

import java.io.File;

/**
 * @author moshi
 */
public class CfgSingleton {
    
    public MonitorCfg config;
    private volatile static CfgSingleton uniqueInstance;
    
    private CfgSingleton() {
        String currentPath = System.getProperty("user.dir") + File.separator + "config.json";
        String configStr = FileReader.readFile(currentPath);
        
        this.config = JSONObject.parseObject(configStr, MonitorCfg.class);
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
