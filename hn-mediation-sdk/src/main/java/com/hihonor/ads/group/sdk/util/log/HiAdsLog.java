/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.adapter.AndroidLogAdapter;
import com.hihonor.ads.group.sdk.util.log.adapter.HandlerLogAdapter;


/**
 * <pre>
 *  ┌────────────────────────────────────────────
 *  │ LOGGER
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Standard logging mechanism
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ But more pretty, simple and powerful
 *  └────────────────────────────────────────────
 * </pre>
 * <p>
 * <h3>如何使用它？</h3>
 * 先初始化
 * <pre><code>
 *   HiAdsLog.addLogAdapter(new AndroidLogAdapter());
 *   HiAdsLog.addLogAdapter(new DiskLogAdapter());
 * </code></pre>
 * <p>
 * 使用适当的静态HiAdsLog方法。
 *
 * <pre><code>
 *   HiAdsLog.d(TAG, "debug");
 *   HiAdsLog.e(TAG, "error");
 *   HiAdsLog.w(TAG, "warning");
 *   HiAdsLog.v(TAG, "verbose");
 *   HiAdsLog.i(TAG, "information");
 *   HiAdsLog.wtf(TAG, "What a Terrible Failure");
 * </code></pre>
 *
 * <h3>支持String格式化参数</h3>
 * <pre><code>
 *   HiAdsLog.d(TAG, "hello %s", "world");
 * </code></pre>
 *
 * <h3>集合支持 （仅用于调试日志）</h3>
 * <pre><code>
 *   HiAdsLog.d(TAG, MAP);
 *   HiAdsLog.d(TAG, SET);
 *   HiAdsLog.d(TAG, LIST);
 *   HiAdsLog.d(TAG, ARRAY);
 * </code></pre>
 *
 * <h3>自定义Log策略</h3>
 * 根据您的需要，您可以更改以下设置：
 * <ul>
 *   <li>Different {@link LogAdapter}</li>
 *   <li>Different {@link FormatStrategy}</li>
 *   <li>Different {@link LogStrategy}</li>
 * </ul>
 *
 * @see LogAdapter
 * @see FormatStrategy
 * @see LogStrategy
 */
public final class HiAdsLog {
    private HiAdsLog() {

    }

    static {
        HiLogger.addLogAdapter(new AndroidLogAdapter());
        HiLogger.addLogAdapter(new HandlerLogAdapter());
    }

    public static void addLogAdapter(@NonNull LogAdapter adapter) {
        HiLogger.addLogAdapter(adapter);
    }

    public static void clearLogAdapters() {
        HiLogger.clearLogAdapters();
    }

    /**
     * 接受所有配置作为参数的一般日志函数
     */
    public static void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable) {
        HiLogger.log(priority, tag, message, throwable);
    }

    public static void d(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        HiLogger.d(tag, message, args);
    }

    public static void d(@NonNull String tag, @Nullable Object object) {
        HiLogger.d(tag, object);
    }

    public static void e(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        HiLogger.e(tag, null, message, args);
    }

    public static void e(@NonNull String tag, @Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        HiLogger.e(tag, throwable, message, args);
    }

    public static void i(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        HiLogger.i(tag, message, args);
    }

    public static void v(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        HiLogger.v(tag, message, args);
    }

    public static void w(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        HiLogger.w(tag, message, args);
    }

    /**
     * 提示：在特殊情况下使用这个来记录
     * 即：意外错误等
     */
    public static void wtf(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        HiLogger.wtf(tag, message, args);
    }
}
