# Vector / Xposed Modules Repo 收录清单

官方提交说明：<https://github.com/Xposed-Modules-Repo/submission>

## 当前收录状态（2026-11-15）

- 提交 issue：[#2600 — `[submission] io.github.seashell666.okokdeclutter`](https://github.com/Xposed-Modules-Repo/submission/issues/2600)
- 机器人结论：`XposedBot` 已关闭 issue，标签为 `approved`，状态原因 `completed`。
- 官方模块仓库：<https://github.com/Xposed-Modules-Repo/io.github.seashell666.okokdeclutter>
- 仓库描述：`Declutter OKOK 3.7.63`
- 主页链接：指向本源码仓库 <https://github.com/seashell666/OKOKDeclutter>
- 官方仓库仅保留 `README.md`、`SUMMARY` 和 Release APK；源码不放在组织仓库。
- 官方仓库 Release：标签 `1-1.0.0`，标题 `1.0.0`，APK 为 `OKOKDeclutter-1.0.0.apk`。
- 镜像 APK 公开下载校验：617,469 字节，SHA-256 为 `1F7943EFAB06E6DD85EA37D9372586D1E397EE62D6005205592E2D591F80E458`。

机器人通过后，应用和网站列表仍可能需要最多 3 个工作日完成展示同步；不能把 issue 通过等同于列表已经立即可见。

## 1. 源码仓库

- 仓库保持公开，包名为 `io.github.seashell666.okokdeclutter`，使用作者自己的 `io.github.seashell666` 前缀。
- APK 的 applicationId、入口类和 `scope.list` 与源码一致。
- 不上传 OKOK 原版 APK、反编译源码、签名密钥或任何账号数据。

## 2. GitHub Release

源码仓库 Release：

- Tag：`1-1.0.0`
- Release title：`1.0.0`
- 附件：已签名的 `OKOKDeclutter-1.0.0.apk`、完整源码 ZIP、`SHA256SUMS.txt`。

官方机器人读取 `versionCode-versionName` 格式。这个项目不加 `v` 前缀，避免生成不支持的标签。

## 3. 提交机器人

官方 `Xposed-Modules-Repo/submission` issue：

- 标题：`[submission] io.github.seashell666.okokdeclutter`
- 正文：`https://github.com/seashell666/OKOKDeclutter`

只有机器人明确返回 `approved` 或成功结论，才表示这一步被接受；本地检查不能代替机器人的结论。

## 4. 收录仓库元数据

机器人在 `Xposed-Modules-Repo` 组织下创建同名模块仓库后，需要放入：

- `SUMMARY`：不超过 30 个英文字符的一句话说明。
- `README.md`：模块介绍、支持版本、源码入口和安装方法。

不要把源代码提交到 `Xposed-Modules-Repo` 组织下的模块仓库；源代码只保留在作者自己的源码仓库。

## 5. 发布后再核对

- Release tag 与 APK 的 versionCode/versionName 完全一致。
- Release APK 能下载，且不是 `app-release-unsigned.apk`。
- Vector 能识别模块，静态作用域只有 `com.chipsea.btcontrol`。
- 摘要与 README 无虚假宣传，不声称已兼容未测试的 OKOK 版本。
- 后续版本发布新 Release 后，机器人会同步版本信息。
