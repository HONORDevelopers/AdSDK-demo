/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.adapter.AndroidLogAdapter;
import com.hihonor.ads.group.sdk.util.log.adapter.DiskLogAdapter;


/**
 * 提供一个公共接口来通过发送日志。这是{@link HiLogger}所需要的合同。
 *
 * @see AndroidLogAdapter 将日志输出到控制台
 * @see DiskLogAdapter 将日志输出到本地文件
 */
public interface LogAdapter {

    /**
     * 用于确定日志是否应该打印出来。
     *
     * @param priority 优先级是日志级别，例如 {@link HiLogger#DEBUG}、{@link HiLogger#WARN}
     * @param tag      是日志消息的给定标签
     * @return 用于确定日志是否应该打印。 如果这是真的，它会被打印出来，否则它会被忽略。
     */

    boolean isLoggable(int priority, @Nullable String tag);

    /**
     * 每个日志将使用此管道
     *
     * @param priority 是日志级别，例如 {@link HiLogger#DEBUG}、{@link HiLogger#WARN}
     * @param tag 是日志消息的给定标签。
     * @param message 是日志消息的给定消息。
     */
    void log(int priority, @Nullable String tag, @NonNull String message);
}