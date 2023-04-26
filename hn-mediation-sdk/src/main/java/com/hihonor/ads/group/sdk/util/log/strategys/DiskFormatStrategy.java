/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log.strategys;

import android.os.HandlerThread;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.hihonor.ads.group.sdk.HnGroupAds;
import com.hihonor.ads.group.sdk.util.log.FormatStrategy;
import com.hihonor.ads.group.sdk.util.log.LogStrategy;
import com.hihonor.ads.group.sdk.util.log.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 本地格式化策略，在写入本地之前，先对日志进行格式化处理后，再交由{@link DiskLogStrategy}
 * 进行本地输出。
 */
public class DiskFormatStrategy implements FormatStrategy {

    private static final String NEW_LINE = System.getProperty("line.separator") == null ? "\n" : System.getProperty("line.separator");
    private static final String NEW_LINE_REPLACEMENT = " <br> ";
    private static final String SEPARATOR = "/";
    private static final String SEPARATOR_2 = ":";
    private static final String SEPARATOR_SPACE = " ";

    @NonNull
    private final Date date;
    @NonNull
    private final SimpleDateFormat dateFormat;
    private final LogStrategy logStrategy;
    private String packageName;

    private DiskFormatStrategy(@NonNull Builder builder) {
        this.date = builder.date;
        this.dateFormat = builder.dateFormat;
        this.logStrategy = builder.logStrategy;
        if (HnGroupAds.get().getContext() != null) {
            this.packageName = HnGroupAds.get().getContext().getPackageName();
        }
    }

    @NonNull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public void log(int priority, @Nullable String onceOnlyTag, @NonNull String message) {
        if (TextUtils.isEmpty(this.packageName) && HnGroupAds.get().getContext() != null) {
            this.packageName = HnGroupAds.get().getContext().getPackageName();
        }
        if (logStrategy == null) {
            return;
        }
//        HnAdConfig adConfig = HnGroupAds.get().getCfg();
//        HnCustomController customController;
//        if (adConfig != null && (customController = adConfig.getCustomController()) != null
//                && !customController.isCanUseWriteLogToDisk()) {
//            return;
//        }

        date.setTime(System.currentTimeMillis());

        StringBuilder builder = new StringBuilder();
        // 拼接时间
        builder.append(dateFormat.format(date));
        // 包名
        builder.append(SEPARATOR);
        builder.append(packageName);
        builder.append(SEPARATOR_SPACE);

        // 拼接等级
        builder.append(Utils.logLevel(priority));
        builder.append(SEPARATOR);
        // 拼接日志 tag
        builder.append(onceOnlyTag);
        // 拼接 message
        if (message.contains(NEW_LINE)) {
            message = message.replaceAll(NEW_LINE, NEW_LINE_REPLACEMENT);
        }
        // 拼接内容
        builder.append(SEPARATOR_2);
        builder.append(SEPARATOR_SPACE);
        builder.append(message);
        // new line
        builder.append(NEW_LINE);
        logStrategy.log(priority, onceOnlyTag, builder.toString());
    }

    public static final class Builder {
        private Date date;
        private SimpleDateFormat dateFormat;
        private LogStrategy logStrategy;

        private Builder() {
        }

        @NonNull
        public Builder date(@Nullable Date val) {
            date = val;
            return this;
        }

        @NonNull
        public Builder dateFormat(@Nullable SimpleDateFormat val) {
            dateFormat = val;
            return this;
        }

        public Builder logStrategy(@Nullable LogStrategy val) {
            logStrategy = val;
            return this;
        }

        @NonNull
        public DiskFormatStrategy build() {
            if (date == null) {
                date = new Date();
            }
            if (dateFormat == null) {
                dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS", Locale.CHINA);
            }
            if (logStrategy == null) {
                HandlerThread handlerThread = new HandlerThread("HiAdsLog");
                handlerThread.start();
                HandlerLogStrategy.WriterHandler handler = new HandlerLogStrategy.WriterHandler(handlerThread.getLooper());
                logStrategy = new HandlerLogStrategy(handler);
            }
            return new DiskFormatStrategy(this);
        }
    }
}
