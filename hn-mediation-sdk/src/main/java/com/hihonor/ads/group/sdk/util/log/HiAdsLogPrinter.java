/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log;



import static com.hihonor.ads.group.sdk.util.log.HiLogger.ASSERT;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.DEBUG;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.ERROR;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.INFO;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.VERBOSE;
import static com.hihonor.ads.group.sdk.util.log.HiLogger.WARN;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.hihonor.ads.group.sdk.util.log.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 广告日志输出Printer，用于封装日志信息及堆栈信息，
 * 调度使用日志适配器清单根据不同策略输出日志。
 */
class HiAdsLogPrinter implements IPrinter {
    private final List<LogAdapter> logAdapters = new ArrayList<>();

    @Override
    public void d(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        log(DEBUG, tag, null, message, args);
    }

    @Override
    public void d(@NonNull String tag, @Nullable Object object) {
        log(DEBUG, tag, null, Utils.toString(object));
    }

    @Override
    public void e(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        e(tag, null, message, args);
    }

    @Override
    public void e(@NonNull String tag, @Nullable Throwable throwable, @NonNull String message, @Nullable Object... args) {
        log(ERROR, tag, throwable, message, args);
    }

    @Override
    public void w(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        log(WARN, tag, null, message, args);
    }

    @Override
    public void i(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        log(INFO, tag, null, message, args);
    }

    @Override
    public void v(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        log(VERBOSE, tag, null, message, args);
    }

    @Override
    public void wtf(@NonNull String tag, @NonNull String message, @Nullable Object... args) {
        log(ASSERT, tag, null, message, args);
    }

    @Override
    public synchronized void log(int priority,
                                 @Nullable String tag,
                                 @Nullable String message,
                                 @Nullable Throwable throwable) {
        if (throwable != null) {
            if (message != null) {
                message += " : " + Utils.getStackTraceString(throwable);
            }
            if (message == null) {
                message = Utils.getStackTraceString(throwable);
            }
        }
        if (TextUtils.isEmpty(message)) {
            message = "Log content is empty or none";
        }
        for (LogAdapter adapter : logAdapters) {
            if (adapter.isLoggable(priority, tag)) {
                adapter.log(priority, tag, message);
            }
        }
    }

    @Override
    public void clearLogAdapters() {
        logAdapters.clear();
    }

    @Override
    public void addAdapter(@NonNull LogAdapter adapter) {
        logAdapters.add(adapter);
    }

    /**
     * 该方法是同步的，以避免混乱的日志顺序。
     */
    private synchronized void log(int priority,
                                  String tag,
                                  @Nullable Throwable throwable,
                                  @NonNull String msg,
                                  @Nullable Object... args) {
        String message = createMessage(msg, args);
        log(priority, tag, message, throwable);
    }

    @NonNull
    private String createMessage(@NonNull String message, @Nullable Object... args) {
        return args == null || args.length == 0 ? message : String.format(message, args);
    }
}
