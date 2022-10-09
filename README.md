## AppleMonitor

一个用 Java 实现的 Apple 线下商店库存监控工具,支持bark,dingtalk，企业微信等监控方式。

## 使用效果

![效果图](docs/images/view.png)

## 如何使用

1. 下载构建的产物压缩包 [releases版本](https://github.com/MoshiCoCo/Apple-Monitor/releases)
2. 解压压缩包，文件内会包含一个可执行的jar和一份config.json配置文件，以及说明文档若干。
3. 编辑config.json配置你需要监控的产品型号以及地区即可，可支持cron表达式自定义监控频率。
4. 执行命令 `java -jar apple-monitor-v0.0.1.jar`

配置文件参数示例

```json
{
  "appleTaskConfig": {
    "cronExpressions": "*/10 * * * * ?",
    "deviceCodes": [
      "MQ0D3CH/A",
      "MPXR3CH/A"
    ],
    "location": "广东 深圳 南山区",
    "storeWhiteList": [
      "益田假日",
      "珠江新城",
      "天环广场"
    ]
  },
  "pushConfig": {
    "barkPushUrl": "https://api.day.app/push",
    "barkPushToken": "bark push token",
    "SC_KEY": "",
    "SCT_KEY": "",
    "TG_BOT_TOKEN": "",
    "TG_USER_ID": "",
    "TG_USE_CUSTOM_URL": false,
    "DING_TALK_URL": "",
    "DING_TALK_SECRET": "",
    "PUSH_PLUS_TOKEN": "",
    "WE_COM_GROUP_TOKEN": "",
    "WE_COM_APP_CORPID": "",
    "WE_COM_APP_CORP_SECRET": "",
    "WE_COM_APP_AGENT_ID": 0,
    "WE_COM_APP_MEDIA_ID": "",
    "WE_COM_APP_TO_USER": "",
    "PROXY_HTTP_HOST": "",
    "PROXY_SOCKET_HOST": "",
    "PROXY_PORT": 0
  }
}
```

| 值               | 含义                                                  |
|-----------------|-----------------------------------------------------|
| cronExpressions | 执行的cron表达式                                          |
| barkPushUrl     | bark推送服务器地址,默认为  https://api.day.app/push           |
| barkPushToken   | bark token    [获取BarkToken请参考](./docs/use-bark.md)  |
| deviceCodes     | 需要监控的产品代码    [产品型号列表](./docs/apple-device-codes.md) |
| location        | 你所在的区域，要用苹果官网风格的地址，例如 广东 深圳 南山区 或者 重庆 重庆 XX区        |
| storeWhiteList  | 商店白名单，一个区域可能有多个商店，仅监控白名单中的商店，模糊匹配，不填则默认监控所有         |

*如何使用Bark请参考 [Bark使用文档](./docs/use-bark.md)*

*苹果产品型号代码请参考 [产品型号列表](./docs/apple-device-codes.md)*

## 支持的推送方式

- 钉钉
- bark
- 企业微信
- server酱