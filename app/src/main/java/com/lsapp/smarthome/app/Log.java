package com.lsapp.smarthome.app;

import com.lsapp.smarthome.BuildConfig;

/**
 * Created by Administrator on 2017/1/10.
 */

public class Log {
    private static final boolean DEBUG = BuildConfig.LOG_DEBUG;

    public static void i(String tag, String msg) {
        if (DEBUG)
            android.util.Log.i(tag, msg);
    }

    public static void e(String tag, String msg) {
        if (DEBUG)
            android.util.Log.e(tag, msg);
    }

    public static void d(String tag, String msg) {
        if (DEBUG)
            android.util.Log.d(tag, msg);
    }

    public static void v(String tag, String msg) {
        if (DEBUG)
            android.util.Log.v(tag, msg);
    }

    public static void w(String tag, String msg) {
        if (DEBUG)
            android.util.Log.w(tag, msg);
    }
}