<div align="center">
<h1 align="center">AppleMonitor</h1>

[![GitHub stars](https://img.shields.io/github/stars/MoshiCoCo/Apple-Monitor?style=flat-square)](https://github.com/MoshiCoCo/Apple-Monitor/stargazers)
[![GitHub forks](https://img.shields.io/github/forks/MoshiCoCo/Apple-Monitor?style=flat-square)](https://github.com/MoshiCoCo/Apple-Monitor/network)
[![GitHub issues](https://img.shields.io/github/issues/MoshiCoCo/Apple-Monitor?style=flat-square)](https://github.com/MoshiCoCo/Apple-Monitor/issues)
[![GitHub license](https://img.shields.io/github/license/MoshiCoCo/Apple-Monitor?style=flat-square)](https://github.com/MoshiCoCo/Apple-Monitor/blob/main/LICENSE)
[![GitHub All Releases](https://img.shields.io/github/downloads/MoshiCoCo/Apple-Monitor/total?style=flat-square)](https://github.com/MoshiCoCo/Apple-Monitor/releases)
[![GitHub release (latest SemVer)](https://img.shields.io/github/v/release/MoshiCoCo/Apple-Monitor?style=flat-square)](https://github.com/MoshiCoCo/Apple-Monitor/releases)
[![Hits](https://hits.seeyoufarm.com/api/count/incr/badge.svg?url=https%3A%2F%2Fgithub.com%2FJunzhouLiu%2FBILIBILI-HELPER-PRE&count_bg=%2379C83D&title_bg=%23555555&icon=&icon_color=%23E7E7E7&title=hits&edge_flat=true)](https://hits.seeyoufarm.com)
</div>

## AppleMonitor

一个用 Java 实现的 Apple 线下商店库存监控工具,支持bark,dingtalk，企业微信等监控方式。

目前已经支持监控中国大陆，中国香港，中国澳门，中国台湾，日本等地区的苹果商店。

Currently, it supports monitoring Apple Stores in mainland China, Hong Kong, Macau, China Taiwan, Japan and other
regions.

## 使用效果

![效果图](docs/images/view.png)

## 如何使用

1. 下载构建的产物压缩包 [releases版本](https://github.com/MoshiCoCo/Apple-Monitor/releases)
2. 解压压缩包，文件内会包含一个可执行的jar和一份config.json配置文件，以及说明文档若干。
3. 编辑config.json配置你需要监控的产品型号以及地区即可，可支持cron表达式自定义监控频率。
4. 执行命令 `java -jar apple-monitor-v0.0.1.jar`

**配置文件参数解释**

| 值               | 含义                                                                |
|-----------------|-------------------------------------------------------------------|
| cronExpressions | 执行的cron表达式,建议执行时间间隔设置为 （监控的设备型号数*3）秒，如果你不会写corn表达式，建议使用程序输出的推荐表达式 |
| barkPushUrl     | bark推送服务器地址,默认为  https://api.day.app/push                         |
| barkPushToken   | bark token    [获取BarkToken请参考](./docs/use-bark.md)                |
| country         | 需要监控的国家，目前仅支持"CN"，"JP" ，CN-MACAO，CN-HK，CN-TW                      |
| location        | 你所在的区域，要用苹果官网风格的地址，例如 广东 深圳 南山区 或者 重庆 重庆 XX区                      |
| deviceCodes     | 需要监控的产品代码    [产品型号列表](./docs/apple-device-codes.md)               |
| storeWhiteList  | 商店白名单，一个区域可能有多个商店，仅监控白名单中的商店，模糊匹配，不填则默认监控所有                       |

注：
如果需要监控日本地区的情况，请将country设置为JP，
location设置为你所在的区域邮编，例如：197-0804，deviceCodes设置为你需要监控的产品型号（日本版本型号），storeWhiteList设置为你需要监控的商店，例如
新宿 ，不填则默认监控所有。


日本地域プロファイル参照例 [config-jp.json](./src/main/resources/config-jp.json)

**配置文件示例**

```json
{
  "appleTaskConfig": {
    "cronExpressions": "*/10 * * * * ?",
    "country": "CN",
    "location": "广东 深圳 南山区",
    "deviceCodes": [
      "MQ0D3CH/A",
      "MPXR3CH/A"
    ],
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

*如何使用Bark请参考 [Bark使用文档](./docs/use-bark.md)*

*苹果产品型号代码请参考 [产品型号列表](./docs/apple-device-codes.md)*

## 支持的推送方式

- 钉钉
- bark
- 企业微信
- server酱

## 致谢

感谢 JetBrains 对本项目的支持。

[![JetBrains](docs/images/jetbrains.svg)](https://www.jetbrains.com/?from=Apple-Monitor)

## License

## Stargazers over time

[![Stargazers over time](https://starchart.cc/MoshiCoCo/Apple-Monitor.svg)](https://starchart.cc/MoshiCoCo/Apple-Monitor)
