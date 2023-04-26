/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log.strategys;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.hihonor.ads.group.sdk.util.log.LogStrategy;
import com.hihonor.ads.group.sdk.util.log.ScheduledExecutor;
import com.hihonor.ads.group.sdk.util.log.utils.LogFileUtils;
import com.hihonor.ads.group.sdk.util.log.utils.ThreadManager;


import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * 日志转存本地策略
 */
public class DiskLogStrategy implements LogStrategy {
    private static final String TAG = DiskLogStrategy.class.getSimpleName();

    /**
     * 桶容量最大阈值 64kb, 64kb指的是日志字符串的内存大小。
     */
    private static final int MAX_SIZE = 64 * 1024;

    /**
     * 轮询间隔时间 30s
     */
    private static final int DELAY_SECONDS = 30;

    /**
     * 当前桶内已使用的大小
     */
    private volatile int bucketSize = 0;

    /**
     * 桶容器
     */
    private final CopyOnWriteArrayList<String> cacheBucket;

    public DiskLogStrategy() {
        this.cacheBucket = new CopyOnWriteArrayList<>();
        // 初始化时，延迟5秒开启定时，间隔30s检查并清理一次桶，无论桶是否装满
        ScheduledExecutor.get().scheduleAtFixedRate(() -> {
                    if (cacheBucket.isEmpty()) {
                        Log.w(TAG, "DiskLogStrategy#scheduleCheckCacheRunnable#cache bucket is empty.");
                        return;
                    }
                    writeLogToDisk();
                },
                DELAY_SECONDS, DELAY_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public void log(int level, @Nullable String tag, @NonNull String message) {
        int length = message.getBytes().length;
        int tempBucketSize = bucketSize;
        // 检查新增数据后，桶是否装满，如果大于等于桶的最大阈值，则写入本地，当前加入的日志不计数在内，新加入的日志
        // 存入桶中等待下次写入。
        if ((tempBucketSize + length) >= MAX_SIZE) {
            writeLogToDisk();
        }
        cacheBucket.add(message);
        bucketSize = tempBucketSize + length;
        Log.i(TAG, "DiskLogStrategy#add log to bucket");
    }

    private synchronized void writeLogToDisk() {
        Log.i(TAG, "DiskLogStrategy#write log to disk");
        Log.i(TAG, "DiskLogStrategy#current bucket size is " + (bucketSize / 1024) + "kb");
        ThreadManager.executeSubLoopThreadTask(new WriterTask(cacheBucket));
        bucketSize = 0;
        cacheBucket.clear();
    }

    static class WriterTask implements Runnable {
        private final CopyOnWriteArrayList<String> cacheBucket;

        public WriterTask(CopyOnWriteArrayList<String> cacheBucket) {
            this.cacheBucket = new CopyOnWriteArrayList<>(cacheBucket);
        }

        @Override
        public void run() {
            if (cacheBucket == null || cacheBucket.isEmpty()) {
                Log.w(TAG, "WriterTask#run cache bucket or folder is null");
                return;
            }
            String[] contents = (String[]) cacheBucket.toArray();
            boolean isWriteComplete = LogFileUtils.writeLogToFile(contents);
            if (isWriteComplete) {
                cacheBucket.clear();
            }
        }
    }
}
