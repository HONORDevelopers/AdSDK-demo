/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log.strategys;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.LogStrategy;
import com.hihonor.ads.group.sdk.util.log.utils.LogFileUtils;


/**
 * 日志转存本地策略
 */
public class HandlerLogStrategy implements LogStrategy {
    private static final String TAG = "HandlerLogStrategy";

    /**
     * 日志存入本地的文件夹位置
     */

    private final WriterHandler mHandler;

    public HandlerLogStrategy(@NonNull WriterHandler handler) {
        this.mHandler = handler;
    }

    @Override
    public void log(int level, @Nullable String tag, @NonNull String message) {
        Log.d(TAG, "log#Send write log msg");
        mHandler.sendMessage(mHandler.obtainMessage(level, message));
    }

    static class WriterHandler extends Handler {
        public WriterHandler(@NonNull Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String content = (String) msg.obj;
            LogFileUtils.writeLogToFile(content);
        }
    }
}
