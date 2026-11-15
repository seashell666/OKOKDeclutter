# OKOK Declutter

一个非官方的 Vector / libxposed Kotlin 模块，用于精简 **OKOK 3.7.63（版本代码 255）** 的指定界面。

## 这个模块有什么用

它只把你不需要的展示区域藏起来，让打开 OKOK、查看首页和体测报告时更直接：

- 开屏广告展示后立即走 OKOK 原本的跳转逻辑，不再等待倒计时或手动点“跳过”。
- 首页顶部活动横幅（截图中的“黑燃片”）不再显示。
- 饮食计划下方的活动横幅（截图中的“放肆吃”）不再显示。
- 首页“打卡送好礼”浮层不再显示。
- 首页左下角“聊一聊”浮窗不再显示。
- 报告页“点击可添加状态”左右动画提示不再显示，右侧真正用于添加状态的铅笔按钮保留。
- 报告页底部广告位不再显示。

广告换文案或换图片不影响效果，因为规则针对的是 OKOK 这一版的控件，而不是广告标题。

## 明确不做什么

- 不修改 OKOK 的账号、登录、隐私协议、体脂数据、饮食记录或主页跳转判断。
- 不破解会员，不修改付费功能，不伪造数据。
- 不屏蔽 OKOK 官方服务器，不拦截或改写健康数据请求。
- 模块自身没有 `INTERNET` 权限，不收集、上传任何信息。
- 不注入其他应用，Vector 作用域只需选择 `com.chipsea.btcontrol`。

## 版本边界

当前只支持 **OKOK 3.7.63（255）**。模块读到的版本不一致时会直接退出，不安装任何 Hook。OKOK 升级后请不要强行继续使用，需要重新核对控件和启动逻辑。

## 验证状态

构建、6 条规则测试、Lint、签名和包内元数据检查均已通过。真机回归已覆盖冷启动、后台切回、首页与报告页指定元素、状态编辑按钮和已有数据刷新，详见 [docs/VALIDATION.md](docs/VALIDATION.md)。

## 怎么安装

1. 确认 OKOK 是 3.7.63（版本代码 255）。
2. 安装模块 APK。
3. 在 Vector 中启用 **OKOK Declutter**，作用域只勾选 **OKOK**。
4. 强行停止 OKOK 后重新打开。之后正常从桌面打开、从后台切回即可。
5. 若想恢复原版，在 Vector 关闭模块或卸载模块，再强行停止并重开 OKOK。

模块没有桌面入口，也不需要单独打开设置。

## 实现原则

- 开屏：保留 `InitActivity` 原有账号检查和广告管理器执行过程，随后调用 OKOK 自己的 `doJump()`，不硬编码“所有人都进主页”。未登录、缺少主角色等情况仍由 OKOK 自己处理。
- 首页和报告页：按“Activity + 控件资源名”双重限定，将目标视图设为 `GONE`，既隐藏画面也收回占位。
- 防误伤：同一资源名出现在其他页面时不处理；正常体重卡片、饮食记录、底部导航和状态编辑按钮不在规则中。
- 防更新失控：只接受版本代码 255，目标方法缺失或单个 Hook 失败时记录日志，不主动扩大匹配范围。

更详细的技术说明见 [docs/IMPLEMENTATION.md](docs/IMPLEMENTATION.md)。

## 本地构建

需要 JDK 17、Android SDK 和 Gradle 8.7。Windows 示例：

```powershell
$env:JAVA_HOME = '你的 JDK 17 路径'
gradle --no-daemon testDebugUnitTest assembleDebug lintDebug
```

Release 签名通过以下环境变量传入，不要把密钥库或密码提交到 Git：

- `OKOKDECLUTTER_KEYSTORE`
- `OKOKDECLUTTER_STORE_PASSWORD`
- `OKOKDECLUTTER_KEY_ALIAS`
- `OKOKDECLUTTER_KEY_PASSWORD`

```powershell
gradle --no-daemon clean testReleaseUnitTest assembleRelease lintRelease
```

Release 附带的完整源码 ZIP 也包含 Gradle Wrapper；使用压缩包构建时，上述 `gradle` 命令可替换为 `.\gradlew.bat`。

## 发布与 Vector 收录

源码仓库 Release 使用标签 **`1-1.0.0`**、标题 **`1.0.0`**，并附带签名 APK。官方收录 issue [#2600](https://github.com/Xposed-Modules-Repo/submission/issues/2600) 已由 `XposedBot` 标记为 `approved`，官方模块仓库位于 <https://github.com/Xposed-Modules-Repo/io.github.seashell666.okokdeclutter>。机器人通过后，应用和网站列表可能需要最多 3 个工作日完成展示同步；完整收录记录见 [docs/VECTOR_SUBMISSION.md](docs/VECTOR_SUBMISSION.md)。

## 免责声明

这是独立、非官方的界面精简模块，与 OKOK 或其开发者没有隶属关系。仅用于改善自己设备上的界面体验；请遵守适用法律和目标应用的使用条款。目标应用更新后，本模块可能不再适用。

## License

[MIT](LICENSE)
