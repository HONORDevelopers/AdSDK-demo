/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 一个代理接口，以启用额外的操作。
 * 包含所有可能的日志消息用法。
 */
public interface IPrinter {
    void addAdapter(@NonNull LogAdapter adapter);

    void d(@NonNull String tag, @NonNull String message, @Nullable Object... args);

    void d(@NonNull String tag, @Nullable Object object);

    void e(@NonNull String tag, @NonNull String message, @Nullable Object... args);

    void e(@NonNull String tag, @Nullable Throwable throwable, @NonNull String message, @Nullable Object... args);

    void w(@NonNull String tag, @NonNull String message, @Nullable Object... args);

    void i(@NonNull String tag, @NonNull String message, @Nullable Object... args);

    void v(@NonNull String tag, @NonNull String message, @Nullable Object... args);

    void wtf(@NonNull String tag, @NonNull String message, @Nullable Object... args);

    void log(int priority, @Nullable String tag, @Nullable String message, @Nullable Throwable throwable);

    void clearLogAdapters();
}
