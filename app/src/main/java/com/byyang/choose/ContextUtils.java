package com.byyang.choose;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import androidx.annotation.NonNull;

import java.lang.reflect.Method;

public class ContextUtils {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    /**
     * @return 反射获取Context
     */
    @NonNull
    public static Context getContext() {
        synchronized (ContextUtils.class) {
            if (mContext == null) {
                try {
                    @SuppressLint("PrivateApi") Class<?> ActivityThread = Class.forName("android.app.ActivityThread");
                    Method method = ActivityThread.getMethod("currentActivityThread");
                    Object currentActivityThread = method.invoke(ActivityThread);//获取currentActivityThread 对象
                    assert currentActivityThread != null;
                    Method method2 = currentActivityThread.getClass().getMethod("getApplication");
                    mContext = (Context) method2.invoke(currentActivityThread);//获取 Context对象
                } catch (Exception e) {
                    Log.e("ContextError", e.getMessage());
                }
            }
        }
        assert mContext != null;
        return mContext;
    }
}
