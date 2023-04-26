/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.FormatStrategy;
import com.hihonor.ads.group.sdk.util.log.strategys.DiskFormatStrategy;


/**
 * 用于将日志消息保存到磁盘。
 * 默认情况下，它使用{@link DiskFormatStrategy }将文本消息转换为txt格式。
 */
public class HandlerLogAdapter extends AbstractLogAdapter {

    @NonNull
    private final FormatStrategy formatStrategy;

    public HandlerLogAdapter() {
        formatStrategy = DiskFormatStrategy.newBuilder().build();
    }

    public HandlerLogAdapter(@NonNull FormatStrategy formatStrategy) {
        this.formatStrategy = formatStrategy;
    }

    @Override
    public void log(int priority, @Nullable String tag, @NonNull String message) {
//        HnAdConfig adConfig = HnGroupAds.get().getCfg();
//        HnCustomController customController;
//        if (adConfig != null && (customController = adConfig.getCustomController()) != null
//                && !customController.isCanUseWriteLogToDisk()) {
//            return;
//        }
        formatStrategy.log(priority, tag, message);
    }
}
