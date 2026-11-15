# 实现说明

## 目标版本

- 包名：`com.chipsea.btcontrol`
- 版本名：3.7.63
- 版本代码：255
- Vector API：102（libxposed）
- 模块包名：`io.github.seashell666.okokdeclutter`

`ModuleEntry` 通过系统上下文的 `PackageManager` 读取宿主真实版本代码。不是 255 时不安装 Hook。

## 启动流程

OKOK 的启动链为：

```text
MainActivity -> InitActivity -> NewMainActivity / LogonActivity
```

`InitActivity.toLoadAd()` 先检查账号，`InitActivity.toLoad()` 再启动开屏广告管理器。模块 Hook `toLoad()`：

1. 执行原方法，不替换 OKOK 的广告管理器和账号检查。
2. 调用 OKOK 自己的私有方法 `doJump()`。
3. `doJump()` 继续按“已登录且有主角色 / 已登录但无主角色 / 未登录”走原来的页面分支。

因此模块没有直接把所有人强送进主页，也没有复制或改写账号逻辑。

## 视图规则

| 页面 | 控件资源名 | 处理 |
| --- | --- | --- |
| `InitActivity` | `ll_container` | 隐藏开屏广告容器，作为快速跳转时的显示兜底 |
| `NewMainActivity` | `banner_card` | 隐藏顶部活动横幅 |
| `NewMainActivity` | `food_banner_view` | 隐藏饮食计划下方活动横幅 |
| `NewMainActivity` | `ai_float_entry` | 隐藏“聊一聊”浮窗 |
| `NewMainActivity` | `pop_suspension_bottom_ll` | 隐藏“打卡送好礼”浮层 |
| `ReportActivity` | `body_status_edit_guide_hint_booble` | 隐藏状态引导气泡，不影响 `body_state_edit_bt` |
| `ReportActivity` / `BodyMeasureReportActivity` | `containerLayout` | 隐藏报告页底部广告容器 |

模块同时 Hook `View.setVisibility()` 和 `View.onAttachedToWindow()`：

- 首次挂载时隐藏，避免目标视图先显示一帧。
- 后续代码再次把它设为可见时仍保持 `GONE`。
- 使用 `GONE` 而不是 `INVISIBLE`，因此不会留下原来的空白占位。

所有判断都要求“当前 Activity 类名 + 控件资源名”同时匹配。资源名解析失败、Activity 无法取得或方法不匹配时默认不处理。

## 网络与隐私

模块不声明网络权限，不建立连接，不调用网络拦截 API，也不读写 OKOK 的请求内容。开屏原方法仍执行；模块只改变展示等待和视图可见性。OKOK 官方账号、健康数据及配置流程继续由原应用负责。

## 日志与故障边界

日志标签为 `OKOKDeclutter`，只记录版本与 Hook 安装/执行异常，不记录账号、身体数据、请求地址或广告内容。单个功能 Hook 安装失败不会扩大 Hook 范围。
