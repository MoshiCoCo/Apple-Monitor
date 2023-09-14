package top.misec.applemonitor.config;

import lombok.Data;


import java.util.List;

@Data
public class DeviceItem {
    private String deviceCode;
    public List<String> storeWhiteList;
    private List<PushConfig> pushConfigs;
}
