package com.locon.withu.utils;

import android.util.Log;

public class Logger {
    public static final boolean DEBUG_FLAG = true;

    public static void logd(Object caller, String text) {
        if (DEBUG_FLAG) {
            String tag = null;
            if (caller instanceof Class) {
                tag = ((Class) caller).getSimpleName();
            } else {
                tag = caller.getClass().getSimpleName();
            }
            if (text != null)
                Log.d(tag, text);
        }
    }
}
