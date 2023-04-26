/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log.strategys;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.LogStrategy;


/**
 * 日志Cat实现 {@link LogStrategy}
 * <p>
 * 使用标准，只需将所有日志打印出来即可，直接使用{@link Log#println(int, String, String)} 进行打印.
 */
public class LogcatStrategy implements LogStrategy {
    static final String DEFAULT_TAG = "NO_TAG";

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
        if (tag == null) {
            tag = DEFAULT_TAG;
        }
        Log.println(priority, tag, message);
    }
}
