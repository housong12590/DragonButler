package com.aosijia.dragonbutler.utils;

import android.util.Log;

public class Logger {

    class BuildConfig {
        public static final boolean DEBUG = true;
    }

    private static String getTagName(Object tag) {
        if (tag instanceof Class) {
            return ((Class<?>) tag).getSimpleName();
        }
        return getTagName(tag.getClass());
    }

    public static void d(Object tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(getTagName(tag), msg);
        }
    }

    public static void e(Object tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(getTagName(tag), msg);
        }
    }

    public static void e(Object tag, String msg, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(getTagName(tag), msg, throwable);
        }
    }

    public static void e(Object tag, Throwable throwable) {
        if (BuildConfig.DEBUG) {
            Log.e(getTagName(tag), "", throwable);
        }
    }

    public static void i(Object tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.i(getTagName(tag), msg);
        }
    }

    public static void w(Object tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.w(getTagName(tag), msg);
        }
    }
}
