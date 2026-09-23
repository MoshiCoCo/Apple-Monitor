## 协作约定

### 提交规范

遵循 [Conventional Commits](https://www.conventionalcommits.org/zh-hans/v1.0.0/)
规范，结合 [Gitmoji](https://gitmoji.dev) 表情符号。

- 一个提交对应一个功能点，保持提交的原子性
- commit message 一律用英文书写，与仓库既有历史保持一致（本文档正文用中文）

**格式：**

```
<emoji> <type>[optional scope]: <description>

[optional body]

[optional footer(s)]
```

**Type 类型与 Gitmoji 映射：**

| Emoji | Type     | 说明                    |
|-------|----------|-------------------------|
| ✨    | feat     | 新功能                  |
| 🐛    | fix      | Bug 修复                |
| 📝    | docs     | 文档变更                |
| 🎨    | style    | 代码格式（不影响逻辑）  |
| ♻️    | refactor | 重构（非 feat、非 fix） |
| ⚡    | perf     | 性能优化                |
| ✅    | test     | 测试相关                |
| 🔧    | chore    | 构建/工具/配置变更      |
| 🔨    | build    | 构建系统或外部依赖变更  |
| 💚    | ci       | CI 配置变更             |
| ⏪    | revert   | 回滚提交                |
| 🔥    | remove   | 删除代码或文件          |
| 🔖    | release  | 发布新版本              |

**示例：**

```
✨ feat(country): add support for SG, MY, AU, UK, CA and US stores
🐛 fix(push): stop silently skipping Feishu bots without a secret
📝 docs(codes): add mainland China part numbers for the iPhone 18 Pro family
♻️ refactor(push): extract a shared push channel abstraction for Bark and Feishu
⚡ perf(monitor): make the per-device interval configurable to avoid idle cron threads
🔧 chore: centralize dependency and plugin versions in pom properties
🔥 remove: drop the retired fulfillment-messages fallback
🔖 release: v0.1.5
```

**Body 编写规范：**

对于非 trivial 的变更，body 应包含变更明细，使用短句列表，并交代清楚「为什么改」而不只是「改了什么」：

```
🐛 fix(monitor): add a global HTTP timeout so monitoring cannot stall silently

- hutool defaults connectionTimeout / readTimeout to -1, so one hung request pins the cron thread forever
- set HttpGlobalConfig.setTimeout(10_000) at startup instead of per request,
  because bark-java-sdk builds its own HttpRequest internally and cannot be injected from outside
- push timeouts no longer bubble up to main and kill the JVM: the startup notice and each channel get their own try/catch
```

**发版提交：**
`release.info` 是 CI 直接拿去当 GitHub Release 正文的文件，写成 `## 中文` 与 `## English`
两段，每段各自编号列出本版本变更，不要在单行里中英混排。提交标题用
`🔖 release: vX.Y.Z`，不需要 body。同时更新 `pom.xml` 的 `<version>` 与
`release.json` 的 `tag_main` / `tag_latest` / `release_date`，三者版本号必须一致，
CI 靠 `tag_main` 找 `target/apple-monitor-${tag_main}.jar`。

**Breaking Changes：**
在 type 后添加 `!` 或在 footer 中说明：

```
✨ feat!: restructure the config.json field names, incompatible with the old format
```
