/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.strategys.DiskFormatStrategy;
import com.hihonor.ads.group.sdk.util.log.strategys.RegularFormatStrategy;


/**
 * 格式策略顶层接口，用于确定如何打印或保存日志。
 *
 * @see RegularFormatStrategy 整洁的格式策略，用于控制台打印时输出格式化边界的日志。
 * @see DiskFormatStrategy 磁盘格式化策略，用于格式化将要写入磁盘的日志。
 */
public interface FormatStrategy {
    void log(int priority, @Nullable String tag, @NonNull String message);
}
