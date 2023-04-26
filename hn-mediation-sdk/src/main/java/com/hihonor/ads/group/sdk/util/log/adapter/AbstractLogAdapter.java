package com.hihonor.ads.group.sdk.util.log.adapter;

import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.HiLogger;
import com.hihonor.ads.group.sdk.util.log.LogAdapter;
import com.hihonor.cloudclient.utils.BuildConfig;


/**
 * 抽象日志适配器
 *
 * @since 2022-10-18
 */
public abstract class AbstractLogAdapter implements LogAdapter {
    @Override
    public boolean isLoggable(int priority, @Nullable String tag) {
        if (BuildConfig.DEBUG) {
            return true;
        }
        return priority != HiLogger.DEBUG;
    }
}
