/*
 * Copyright (c) Honor Device Co., Ltd. 2021-2022. All rights reserved.
 */

package com.hihonor.ads.group.sdk.util.log;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 任务执行器，用于执行异步任务，可切换主线程，任务按顺序添加如任务队列，按顺序执行，内部维护线程池处理并发操作，
 *
 * @since 2022-09-22
 */
public final class ScheduledExecutor {
    private static final int CPU_ACTIVE_COUNT = Runtime.getRuntime().availableProcessors();

    private static final int CORE_POOL_SIZE = 2 * CPU_ACTIVE_COUNT;

    private final ScheduledThreadPoolExecutor EXECUTOR;

    public static ScheduledExecutor get() {
        return SingleHolder.INSTANCE;
    }

    private static class SingleHolder {
        public static final ScheduledExecutor INSTANCE = new ScheduledExecutor();
    }

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "HiAdsTimer #" + mCount.getAndIncrement());
        }
    };

    public void schedule(Runnable command, long delay, TimeUnit unit) {
        EXECUTOR.schedule(command, delay, unit);
    }

    public void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        EXECUTOR.scheduleAtFixedRate(command, initialDelay, period, unit);
    }

    private ScheduledExecutor() {
        EXECUTOR = new ScheduledThreadPoolExecutor(CORE_POOL_SIZE, sThreadFactory);
        EXECUTOR.setKeepAliveTime(1, TimeUnit.SECONDS);
        EXECUTOR.allowCoreThreadTimeOut(true);
    }

    public void shutdown() {
        EXECUTOR.shutdown();
    }
}
