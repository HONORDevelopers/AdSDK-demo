/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.FormatStrategy;
import com.hihonor.ads.group.sdk.util.log.strategys.RegularFormatStrategy;


/**
 * Android终端日志输出实现 {@link LogAdapter}.
 * <p>
 * 打印输出到LogCat与整齐的边界.
 *
 * <pre>
 *  ┌──────────────────────────
 *  │ 线程信息
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ 日志信息
 *  └──────────────────────────
 * </pre>
 */
public class AndroidLogAdapter extends AbstractLogAdapter {

    @NonNull
    private final FormatStrategy formatStrategy;

    public AndroidLogAdapter() {
        this.formatStrategy = RegularFormatStrategy.newBuilder().build();
    }

    public AndroidLogAdapter(@NonNull FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        formatStrategy.log(priority, tag, message);
    }

}
