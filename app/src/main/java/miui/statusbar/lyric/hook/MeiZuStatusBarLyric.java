package miui.statusbar.lyric.hook;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.app.Notification;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import miui.statusbar.lyric.utils.Utils;

public class MeiZuStatusBarLyric {

    @SuppressLint("StaticFieldLeak")
    static Context context;

    //模拟flyme
    public static void guiseFlyme(XC_LoadPackage.LoadPackageParam lpparam, boolean hookNotification) {
        // 获取Context
        XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                context = (Context) param.args[0];
            }
        });
        XposedHelpers.findAndHookMethod("android.os.SystemProperties", lpparam.classLoader, "get", String.class, String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedHelpers.setStaticObjectField(Build.class, "BRAND", "meizu");
                XposedHelpers.setStaticObjectField(Build.class, "MANUFACTURER", "Meizu");
                XposedHelpers.setStaticObjectField(Build.class, "DEVICE", "m1892");
                XposedHelpers.setStaticObjectField(Build.class, "DISPLAY", "Flyme");
                XposedHelpers.setStaticObjectField(Build.class, "PRODUCT", "meizu_16thPlus_CN");
                XposedHelpers.setStaticObjectField(Build.class, "MODEL", "meizu 16th Plus");
            }
        });
        XposedHelpers.findAndHookMethod("java.lang.Class", lpparam.classLoader, "getDeclaredField", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
//                XposedBridge.log("getDeclaredField: " + param.args[0]);
                if (param.args[0].toString().equals("FLAG_ALWAYS_SHOW_TICKER")) {
                    param.setResult(MeiZuNotification.class.getDeclaredField("FLAG_ALWAYS_SHOW_TICKER_HOOK"));
                } else if (param.args[0].toString().equals("FLAG_ONLY_UPDATE_TICKER")) {
                    param.setResult(MeiZuNotification.class.getDeclaredField("FLAG_ONLY_UPDATE_TICKER_HOOK"));
                }
            }
        });
        XposedHelpers.findAndHookMethod("java.lang.Class", lpparam.classLoader, "getField", String.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
//                XposedBridge.log("getDeclaredField: " + param.args[0]);
                if (param.args[0].toString().equals("FLAG_ALWAYS_SHOW_TICKER")) {
                    param.setResult(MeiZuNotification.class.getField("FLAG_ALWAYS_SHOW_TICKER_HOOK"));
                } else if (param.args[0].toString().equals("FLAG_ONLY_UPDATE_TICKER")) {
                    param.setResult(MeiZuNotification.class.getField("FLAG_ONLY_UPDATE_TICKER_HOOK"));
                }
            }
        });
        if (!hookNotification) {
            return;
        }
        XposedHelpers.findAndHookMethod("android.app.NotificationManager", lpparam.classLoader, "notify", int.class, Notification.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Notification notification = ((Notification) param.args[1]);
                CharSequence charSequence = notification.tickerText;
                Utils.log(notification.toString());
                if (notification.flags == 0) {
                    Utils.sendLyric(context, "", Utils.packName_GetIconName(lpparam.packageName));
                    return;
                }
                boolean isLyric = (notification.flags & MeiZuNotification.FLAG_ALWAYS_SHOW_TICKER) != 0 || (notification.flags & MeiZuNotification.FLAG_ONLY_UPDATE_TICKER) != 0;
                if (charSequence == null || !isLyric) {
                    return;
                }
                Utils.sendLyric(context, charSequence.toString(), Utils.packName_GetIconName(lpparam.packageName));
            }
        });
    }
}