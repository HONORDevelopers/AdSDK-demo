package com.hihonor.ads.group.sdk.util.log.utils;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


public class ThreadManager {
    private final static int CORE_SIZE = Runtime.getRuntime().availableProcessors();
    private final static int MAX_SIZE = 50;
    private final static int KEEP_ALIVE_TIME = 60;
    private final static int QUEUE_SIZE = 50;
    private static ScheduledThreadPoolExecutor businessService;
    private static ThreadPoolExecutor rxjavaService;

    private static ConcurrentHashMap<Runnable, Future> taskCacheMap = new ConcurrentHashMap();

    private static volatile AtomicBoolean isInit = new AtomicBoolean(false);
    private static final String NAME_BUSINESS_POOL = "business";
    private static final String NAME_RXJAVA_POOL = "rxjava";
    private static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    private static volatile Handler SUB_THREAD_HANDLER;
    private static volatile HandlerThread SUB_THREAD;

    static  {
        if (isInit.compareAndSet(false, true)) {
            businessService = new ScheduledThreadPoolExecutor(CORE_SIZE, getThreadFactory(NAME_BUSINESS_POOL));
            rxjavaService = new ThreadPoolExecutor(CORE_SIZE,
                    MAX_SIZE, KEEP_ALIVE_TIME,
                    TimeUnit.SECONDS,
                    new ArrayBlockingQueue<Runnable>(QUEUE_SIZE), getThreadFactory(NAME_RXJAVA_POOL));
            SUB_THREAD = new HandlerThread("LOOP_SUB");
            SUB_THREAD.start();
            SUB_THREAD_HANDLER = new Handler(SUB_THREAD.getLooper());
        }
    }

    private static ThreadFactory getThreadFactory(final String poolName) {
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "pool-".concat(poolName).concat("-thread"));
            }
        };
    }


    public static synchronized void cancel(@NonNull Runnable task) {
        checkInit();
        if (taskCacheMap.contains(task)) {
            Future future = taskCacheMap.get(task);
            if (future != null && !future.isCancelled() && !future.isDone()) {
                future.cancel(true);
                taskCacheMap.remove(task);
            }
        }
        mainThreadHandler.removeCallbacks(task);

    }

    public static synchronized void shutDownAll() {
        if (isInit.compareAndSet(true, false)) {
            if (!businessService.isShutdown()) {
                businessService.shutdownNow();
            }
            if (!rxjavaService.isShutdown()) {
                rxjavaService.shutdownNow();
            }
            if (taskCacheMap != null) {
                taskCacheMap.clear();
                taskCacheMap = null;
            }
            if (SUB_THREAD != null) {
                SUB_THREAD.quitSafely();
                SUB_THREAD = null;
                SUB_THREAD_HANDLER = null;
            }
        }
    }

    private static synchronized void checkInit() {
        if (!isInit.get()) {
            Log.e("ThreadManager", "excutor not init");
        }
    }

    /**
     * 执行耗时任务
     *
     * @param task
     */
    public static synchronized void excuteTask(@NonNull Runnable task) {
        checkInit();
        executeTaskDelay(task, 0L);
    }

    /**
     * 延迟执行任务
     *
     * @param task  任务
     * @param delay 延迟时间毫秒值
     */
    public static synchronized void executeTaskDelay(final Runnable task, long delay) {
        checkInit();
        taskCacheMap.put(task, businessService.schedule(new ThreadCenterRunnable(task), delay, TimeUnit.MILLISECONDS));
    }

    /**
     * 主线程执行任务
     *
     * @param task 任务
     */
    public static synchronized void executeMainThreadTask(Runnable task) {
        checkInit();
        executeMainThreadTask(task, 0L);
    }

    /**
     * 主线程执行任务
     *
     * @param task  任务
     * @param delay 延迟时间
     */
    public static synchronized void executeMainThreadTask(Runnable task, long delay) {
        checkInit();
        mainThreadHandler.postDelayed(task, delay);
    }

    public static synchronized void executeSubLoopThreadTask(Runnable task) {
        executeSubLoopThreadTask(task, 0L);
    }

    public static synchronized void executeSubLoopThreadTask(Runnable task, long delay) {
        checkInit();
        SUB_THREAD_HANDLER.postDelayed(task, delay);
    }

    private static class ThreadCenterRunnable implements Runnable {
        Runnable task;

        ThreadCenterRunnable(@NonNull Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            task.run();
            taskCacheMap.remove(task);
        }
    }
}
