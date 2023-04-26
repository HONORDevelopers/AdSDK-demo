/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 日志打印
 */
public final class HiLogger {

    public static final int VERBOSE = 2;
    public static final int DEBUG = 3;
    public static final int INFO = 4;
    public static final int WARN = 5;
    public static final int ERROR = 6;
    public static final int ASSERT = 7;

    @NonNull
    private static IPrinter IPrinter = new HiAdsLogPrinter();

    private HiLogger() {
        //no instance
    }

    public static void printer(@NonNull IPrinter IPrinter) {
        HiLogger.IPrinter = IPrinter;
    }

    public static void addLogAdapter(@NonNull LogAdapter adapter) {
        IPrinter.addAdapter(adapter);
    }

    public static void clearLogAdapters() {
        IPrinter.clearLogAdapters();
    }

    /**
     * 接受所有配置作为参数的一般日志函数
     */
    public static void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
        IPrinter.log(priority, tag, message, throwable);
    }

    public static void d(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        IPrinter.d(tag, message, args);
    }

    public static void d(@NonNull String tag, @Nullable Object object) {
        IPrinter.d(tag, object);
    }

    public static void e(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        IPrinter.e(tag, null, message, args);
    }

    public static void e(@NonNull String tag, @Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        IPrinter.e(tag, throwable, message, args);
    }

    public static void i(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        IPrinter.i(tag, message, args);
    }

    public static void v(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        IPrinter.v(tag, message, args);
    }

    public static void w(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        IPrinter.w(tag, message, args);
    }

    /**
     * 提示：在特殊情况下使用这个来记录
     * 即：意外错误等
     */
    public static void wtf(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        IPrinter.wtf(tag, message, args);
    }
}
