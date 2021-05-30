package com.f11st.cutmiuiinstaller;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.provider.Settings;
import android.view.View;
import android.app.Activity;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;
import static de.robv.android.xposed.XposedHelpers.getObjectField;
import static de.robv.android.xposed.XposedHelpers.setObjectField;

public class InstallerHook implements IXposedHookLoadPackage {
    public void handleLoadPackage(final LoadPackageParam lpparam) throws Throwable {
        if (!lpparam.packageName.equals("com.miui.packageinstaller")) {
            return;
        }
        XposedBridge.log("Loaded app: " + lpparam.packageName);

        // 去除安装完成返回商店
        findAndHookMethod("com.miui.packageInstaller.InstallProgressActivity", lpparam.classLoader,
                "v",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        setObjectField(param.thisObject, "C", null);
                    }
                });

        // 去除安全提示
        findAndHookMethod("com.miui.packageInstaller.NewPackageInstallerActivity", lpparam.classLoader,
                "w",
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        setObjectField(param.thisObject, "la", null);
                    }
                });

//        // 去除按钮中图标
//        findAndHookMethod("com.miui.packageInstaller.ui.InstallerActionBar", lpparam.classLoader,
//                "a",
//                CharSequence.class,
//                boolean.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        View b = (View) getObjectField(param.thisObject, "h");
//                        b.setVisibility(View.GONE);
//                    }
//                });

//        // 修改按钮比重
//        findAndHookMethod("com.miui.packageInstaller.ui.InstallerActionBar", lpparam.classLoader,
//                "a",
//                float.class,
//                float.class,
//                new XC_MethodHook() {
//                    @Override
//                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
//                        param.args[0] = 0.6f;
//                        param.args[1] = 0.4f;
//
//                        View left = (View) getObjectField(param.thisObject, "a");
//                        left.setBackgroundResource(2131230891);
//                        TextView leftView = (TextView) getObjectField(param.thisObject, "e");
//                        leftView.setTextColor(Color.parseColor("#ffffff"));
//                        View right = (View) getObjectField(param.thisObject, "b");
//                        right.setBackgroundResource(2131230892);
//                        TextView rightView = (TextView) getObjectField(param.thisObject, "g");
//                        rightView.setTextColor(0x4d000000);
//                    }
//                });

//        // 去除第一页内容
//        findAndHookMethod("com.miui.packageInstaller.NewPackageInstallerActivity", lpparam.classLoader,
//                "J",
//                new XC_MethodHook() {
//                    @Override
//                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                        View main = (View) getObjectField(param.thisObject, "M");
//                        main.setVisibility(View.INVISIBLE);
//                    }
//                });

        // 简化安装二次确认
        findAndHookMethod("com.miui.packageInstaller.NewPackageInstallerActivity", lpparam.classLoader,
                "a",
                List.class,
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        XposedBridge.log("replace com.miui.packageInstaller.NewPackageInstallerActivity a()");
                        XposedHelpers.callMethod(param.thisObject, "D");
                        return null;
                    }
                });

        // 解锁第三方应用安装系统软件
        findAndHookMethod("com.android.packageinstaller.ya", lpparam.classLoader,
                "f",
                Context.class,
                String.class,
                new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(XC_MethodHook.MethodHookParam param) throws Throwable {
                        XposedBridge.log("hook com.android.packageinstaller.ya f()");
                        param.setResult(true);
                    }
                });


    }
}
