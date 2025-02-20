package miui.statusbar.lyric.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import de.robv.android.xposed.XSharedPreferences;
import miui.statusbar.lyric.config.ApiListConfig;
import miui.statusbar.lyric.config.Config;

public class ConfigUtils {

    XSharedPreferences xSP;
    SharedPreferences SP;
    SharedPreferences.Editor SPEditor;

    public ConfigUtils(XSharedPreferences xSharedPreferences) {
        xSP = xSharedPreferences;
        SP = xSharedPreferences;
    }

    @SuppressLint("CommitPrefEdits")
    public ConfigUtils(SharedPreferences SharedPreferences) {
        SP = SharedPreferences;
        SPEditor = SharedPreferences.edit();
    }

    public static ApiListConfig getAppList(Context context) {
        try {
            return new ApiListConfig(getSP(context, "AppList_Config"));
        } catch (SecurityException ignored) {
            return null;
        }
    }

    public static Config getConfig(Context context) {
        try {
            return new Config(getSP(context, "Lyric_Config"));
        } catch (SecurityException ignored) {
            return null;
        }
    }
  public static Config getIconConfig(Context context) {
        try {
            return new Config(getSP(context, "Icon_Config"));
        } catch (SecurityException ignored) {
            return null;
        }
    }

    public static SharedPreferences getSP(Context context, String key) {
        return context.createDeviceProtectedStorageContext().getSharedPreferences(key, Context.MODE_WORLD_READABLE);
    }

    public void update() {
        if (xSP == null) {
            xSP = Utils.getPref("Lyric_Config");
            SP = xSP;
            return;
        }
        xSP.reload();
    }

    public void put(String key, Object value) {
        if (value instanceof Integer) {
            SPEditor.putInt(key, Integer.parseInt(value.toString())).apply();
        } else if (value instanceof String) {
            SPEditor.putString(key, value.toString()).apply();
        } else if (value instanceof Boolean) {
            SPEditor.putBoolean(key, (boolean) value).apply();
        } else if (value instanceof Float) {
            SPEditor.putFloat(key, Float.parseFloat(value.toString())).apply();
        }
    }

    public int optInt(String key, int i) {
        if (SP == null) {
            return i;
        }
        return SP.getInt(key, i);
    }

    public Boolean optBoolean(String key, boolean bool) {
        if (SP == null) {
            return bool;
        }
        return SP.getBoolean(key, bool);
    }

    public String optString(String key, String str) {
        if (SP == null) {
            return str;
        }
        return SP.getString(key, str);
    }

    public Float optFloat(String key, float f) {
        if (SP == null) {
            return f;
        }
        return SP.getFloat(key, f);
    }

    public void clearConfig() {
        SPEditor.clear().apply();
    }
}

