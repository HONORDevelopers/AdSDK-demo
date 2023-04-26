/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.strategys.DiskLogStrategy;
import com.hihonor.ads.group.sdk.util.log.strategys.LogcatStrategy;


/**
 * 确定日志的目的地目标，如磁盘，日志等策略
 *
 * @see LogcatStrategy
 * @see DiskLogStrategy
 */
public interface LogStrategy {

    /**
     * 每次处理日志消息时， Logger都会调用此操作。
     * 将这种方法解释为整个管道中日志的最后目的地。
     *
     * @param priority 日志级别，例如。 {@link HiLogger#DEBUG}, {@link HiLogger#WARN}
     * @param tag      日志消息的标记。
     * @param message  日志消息的给定消息。
     */
    void log(int priority, @Nullable String tag, @NonNull String message);
}
