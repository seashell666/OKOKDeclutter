# 验证记录

## 构建与包内检查

- 目标应用：OKOK 3.7.63，版本代码 255，包名 `com.chipsea.btcontrol`。
- Release 构建：成功。
- 单元测试：6 条全部通过，覆盖版本限制、全部指定控件、报告页编辑按钮保留和同名控件防误伤。
- Android Lint：0 个错误。剩余 2 个非阻塞警告：旧版 AGP 对 libxposed 自定义 Lint 的兼容提示，以及 Android 12+ 备份规则在旧系统会被忽略的提示。
- APK applicationId：`io.github.seashell666.okokdeclutter`。
- APK 版本：versionCode `1`，versionName `1.0.0`；与 Release 标签 `1-1.0.0` 对应。
- 签名：RSA 4096，APK Signature Scheme v2 验证通过。
- 权限：`aapt2 dump permissions` 只有包名，没有 `INTERNET` 或其他权限声明。
- Vector 包内文件：`java_init.list`、`module.prop`、`scope.list` 均存在；静态作用域只有 `com.chipsea.btcontrol`。
- 最终 APK SHA-256：`1F7943EFAB06E6DD85EA37D9372586D1E397EE62D6005205592E2D591F80E458`。
- 真机安装与覆盖升级：`adb install -r` 均返回 `Success`。

## 真机回归：已通过

测试设备为连接的小米手机，Vector 已启用模块且作用域只有 OKOK。

1. **版本保护与注入**
   - 首次真机测试发现 `onPackageReady` 阶段宿主 Application 尚未创建，导致版本读取失败。
   - 已改为通过 `ActivityThread` 的系统上下文读取 PackageManager，重新构建后日志明确显示 `active for OKOK 3.7.63 (255)`。
   - 清空旧日志后完成冷启动、页面跳转和后台切回，未再出现版本读取失败或 Hook 安装/执行异常。

2. **冷启动**
   - 强行停止 OKOK 后从桌面启动，按 250 毫秒间隔连续记录前台 Activity。
   - 约 500 毫秒为 `MainActivity`，约 1000 毫秒已进入 `NewMainActivity`；后续 3 秒采样均保持主页。
   - 整段采样没有经过 `InitActivity`，不再等待开屏广告倒计时。

3. **后台切回**
   - 在主页按 Home，再从桌面重新进入，前台直接恢复 `NewMainActivity`，没有重新出现开屏页。

4. **首页四处处理**
   - 真机截图确认两个活动横幅、打卡浮层和聊一聊浮窗均不显示。
   - 体重、热量记录、目标、体重趋势、喝水和底部导航仍正常显示，广告原位置没有留下大块空白。

5. **报告页两处处理**
   - 真机截图确认状态动画提示和底部广告位均不显示。
   - `body_state_edit_bt` 铅笔按钮仍可见；点击后成功打开“添加状态”页面，心情、运动强度、排便次数、特殊情况、备注和确定按钮均存在。
   - 验证后仅返回，没有保存或修改任何身体记录。

6. **数据与正常功能**
   - 首页和报告页均能加载已有体重及身体指标。
   - 点击报告页刷新后仍停留在正常报告页，数据展示无异常。
   - 模块没有网络权限，也没有网络拦截或请求改写代码，官方数据路径未被模块接管。
   - 本次未连接实体体脂秤做新增测量，也未做服务器端数据库比对；验证范围是已存数据加载、报告刷新和正常页面功能。

## 发布边界

真机回归已完成，可以创建公开 GitHub Release 并提交收录机器人。机器人是否接受以其返回结果为准，本地验证不替代官方审查。
