# Cut MIUI Installer

本项目为xposed模块，针对新版MIUI安装器做出修改(版本号>=3.0.4)

- 可以在其他应用里面安装系统应用
- 删除未上架应用提示
- 删除安装二次确认
- 删除安装完成后返回键跳转商店
- 删除无用内容
- 恢复显示安装完成后“完成”按钮

使用edxposed或lsposed等具有白名单的xposed框架时，向白名单中添加添加安装管理（com.miui.packageinstaller）