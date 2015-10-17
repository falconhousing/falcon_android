package com.locon.withu.utils;

import android.content.Context;
import android.content.SharedPreferences;

public final class PrefsHelper {

    private PrefsHelper() {

    }

    private static final String CONFIG_PREFS_FILE = "Preferences";
    private static SharedPreferences sPreferences;

    public static void init(Context context) {
        sPreferences = context.getSharedPreferences(CONFIG_PREFS_FILE, Context.MODE_PRIVATE);
    }

    public static void putString(String key, String value) {
        sPreferences.edit().putString(key, value).apply();
    }

    public static String getString(String key, String defaultValue) {
        return sPreferences.getString(key, defaultValue);
    }

    public static void putInt(String key, int value) {
        sPreferences.edit().putInt(key, value).apply();
    }

    public static int getInt(String key, int defaultValue) {
        return sPreferences.getInt(key, defaultValue);
    }

    public static void putLong(String key, long value) {
        sPreferences.edit().putLong(key, value).apply();
    }

    public static long getLong(String key, long defaultValue) {
        return sPreferences.getLong(key, defaultValue);
    }

    public static void remove(String key) {
        sPreferences.edit().remove(key).apply();
    }

    public static void clear() {
        sPreferences.edit().clear().apply();
    }
}
