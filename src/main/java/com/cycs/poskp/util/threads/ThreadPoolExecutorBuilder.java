package com.cycs.poskp.util.threads;

import java.util.concurrent.TimeUnit;

/**
 * 线程池工厂类
 *
 * @see ThreadPoolExecutor
 */
public class ThreadPoolExecutorBuilder {

    /**
     * 线程名前缀
     */
    private static String namePrefix = "task-pool-";
    /**
     * 初始线程大小
     */
    private static Integer coreSize = 10;
    /**
     * 线程最大值
     */
    private static Integer maxSize = 50;
    /**
     * 线程队列大小
     */
    private static Integer queueSize = 100;
    /**
     * 空闲存活时间
     */
    private static Integer keepAliveTime = 60;
    /**
     * 启动初始化线程
     */
    protected static boolean preStartMinSpareThreads = true;

    /**
     * 获取线程池实例
     * 默认提供一个: {@code namePrefix} = "task-pool-", {@code coreSize} = 10, {@code maxSize} = 20,
     * {@code queueSize} = 100 的线程池
     * 启动初始线程, 空闲存活时间60s
     *
     * @return ThreadPoolExecutor 线程池实例
     * @see #getInstance(int, String, int, int, int, boolean)
     */
    public static ThreadPoolExecutor getInstance() {
        return getInstance(queueSize, namePrefix, coreSize, maxSize);
    }

    /**
     * 获取指定大小的线程池实例
     * 启动初始线程, 空闲存活时间60s
     * @param queueSize                 队列大小
     * @param namePrefix                名称前缀
     * @param coreSize                  初始线程池
     * @param maxSize                   线程池最大值
     * @return ThreadPoolExecutor 线程池实例
     */
    public static ThreadPoolExecutor getInstance(int queueSize, String namePrefix, int coreSize, int maxSize) {
        return getInstance(queueSize, namePrefix, coreSize, maxSize, keepAliveTime, preStartMinSpareThreads);
    }

    /**
     * 获取指定大小的线程池实例
     *
     * @param queueSize                 队列大小
     * @param namePrefix                名称前缀
     * @param coreSize                  初始线程池
     * @param maxSize                   线程池最大值
     * @param keepAliveTime             存活时间
     * @param isPreStartMinSpareThreads 启动初始化线程
     * @return ThreadPoolExecutor 线程池实例
     */
    public static ThreadPoolExecutor getInstance(int queueSize, String namePrefix, int coreSize,
                                                 int maxSize, int keepAliveTime, boolean isPreStartMinSpareThreads) {
        final TaskQueue taskQueue = new TaskQueue(queueSize);
        TaskThreadFactory factory = new TaskThreadFactory(namePrefix, true, Thread.NORM_PRIORITY);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                coreSize, maxSize, keepAliveTime, TimeUnit.MILLISECONDS, taskQueue, factory);
        //默认为 Constants.DEFAULT_THREAD_RENEWAL_DELAY 非必须
        threadPoolExecutor.setThreadRenewalDelay(Constants.DEFAULT_THREAD_RENEWAL_DELAY);
        //初始化线程
        if (isPreStartMinSpareThreads) {
            threadPoolExecutor.prestartAllCoreThreads();
        }
        taskQueue.setParent(threadPoolExecutor);
        return threadPoolExecutor;
    }
}
