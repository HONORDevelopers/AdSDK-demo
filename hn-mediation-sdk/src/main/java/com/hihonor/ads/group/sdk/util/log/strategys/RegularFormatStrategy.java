/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log.strategys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.FormatStrategy;
import com.hihonor.ads.group.sdk.util.log.LogStrategy;


/**
 * 在给定的日志消息周围绘制边界，并附加一些信息，如：
 *
 * <ul>
 *   <li>线程信息</li>
 * </ul>
 *
 * <pre>
 *  ┌──────────────────────────
 *  │ Thread information
 *  ├┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄
 *  │ Log message
 *  └──────────────────────────
 * </pre>
 *
 * <h3>如何使用？</h3>
 * <pre><code>
 *   FormatStrategy formatStrategy = RegularFormatStrategy.newBuilder()
 *       .showThreadInfo(false)  // (可选) 是否显示线程信息。默认正确
 *       .logStrategy(customLog) // (可选) 更改日志策略以打印出来。默认日志卡
 *       .build();
 * </code></pre>
 */
public class RegularFormatStrategy implements FormatStrategy {

    /**
     * Android对日志条目的最大限制是～4076字节，
     * 所以4000字节被用作块大小，因为默认charset是UTF-8
     */
    private static final int CHUNK_SIZE = 4000;

    /**
     * Drawing toolbox
     */
    private static final char TOP_LEFT_CORNER = '┌';
    private static final char BOTTOM_LEFT_CORNER = '└';
    private static final char MIDDLE_CORNER = '├';
    private static final char HORIZONTAL_LINE = '│';
    private static final String DOUBLE_DIVIDER = "────────────────────────────────────────────────────────";
    private static final String SINGLE_DIVIDER = "┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;
    private static final String COMMON_TAG_ = "COMMON-TAG-";

    private final boolean showThreadInfo;
    @NonNull
    private final LogStrategy logStrategy;

    private RegularFormatStrategy(@NonNull Builder builder) {
        showThreadInfo = builder.showThreadInfo;
        this.logStrategy = builder.logStrategy;
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void log(int priority, @Nullable String onceOnlyTag, @NonNull String message) {
        String tag = COMMON_TAG_ + onceOnlyTag;
        logTopBorder(priority, tag);
        if (showThreadInfo) {
            logChunk(priority, tag, HORIZONTAL_LINE + " Thread: " + Thread.currentThread().getName());
            logDivider(priority, tag);
        }
        // 用系统默认charset （ Android的UTF-8）获取字节消息
        byte[] bytes = message.getBytes();
        int length = bytes.length;
        if (length <= CHUNK_SIZE) {
            logContent(priority, tag, message);
            logBottomBorder(priority, tag);
            return;
        }
        for (int i = 0; i < length; i += CHUNK_SIZE) {
            int count = Math.min(length - i, CHUNK_SIZE);
            // 创建一个带有系统默认charset （即Android的UTF-8）的新字符串
            logContent(priority, tag, new String(bytes, i, count));
        }
        logBottomBorder(priority, tag);
    }

    private void logTopBorder(int logType, @Nullable String tag) {
        logChunk(logType, tag, TOP_BORDER);
    }

    private void logBottomBorder(int logType, @Nullable String tag) {
        logChunk(logType, tag, BOTTOM_BORDER);
    }

    private void logDivider(int logType, @Nullable String tag) {
        logChunk(logType, tag, MIDDLE_BORDER);
    }

    private void logContent(int logType, @Nullable String tag, @NonNull String chunk) {
        String[] lines = chunk.split("\n");
        for (String line : lines) {
            logChunk(logType, tag, HORIZONTAL_LINE + " " + line);
        }
    }

    private void logChunk(int priority, @Nullable String tag, @NonNull String chunk) {
        logStrategy.log(priority, tag, chunk);
    }

    public static class Builder {
        boolean showThreadInfo = true;
        @Nullable
        LogStrategy logStrategy;

        private Builder() {
        }

        @NonNull
        public Builder showThreadInfo(boolean val) {
            showThreadInfo = val;
            return this;
        }

        @NonNull
        public Builder logStrategy(@NonNull LogStrategy val) {
            logStrategy = val;
            return this;
        }

        @NonNull
        public RegularFormatStrategy build() {
            if (logStrategy == null) {
                logStrategy = new LogcatStrategy();
            }
            return new RegularFormatStrategy(this);
        }
    }
}
