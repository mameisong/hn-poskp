package com.cycs.poskp.task.pool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.cycs.poskp.util.threads.ThreadPoolExecutor;
import com.cycs.poskp.util.threads.ThreadPoolExecutorBuilder;

@Component
public class GrabInvoiceThreadPool {
	private static Logger logger = LoggerFactory.getLogger(GrabInvoiceThreadPool.class);

    /**
     * 线程名前缀
     */
    @Value("${pool-thread.template-msg-send.name-prefix:grab-pool-}")
    private String namePrefix;
    /**
     * 初始线程大小
     */
    @Value("${pool-thread.template-msg-send.core-size:2}")
    private Integer coreSize;
    /**
     * 线程最大值
     */
    @Value("${pool-thread.template-msg-send.max-size:50}")
    private Integer maxSize;
    /**
     * 线程队列大小
     */
    @Value("${pool-thread.template-msg-send.queue-size:100}")
    private Integer queueSize;
    /**
     * 存活时间
     */
    @Value("${pool-thread.template-msg-send.keep-alive-time:60}")
    private Integer keepAliveTime;
    /**
     * 关闭等待时间
     */
    @Value("${pool-thread.template-msg-send.await-termination-seconds:60}")
    private int awaitTerminationSeconds;
    /**
     * 是否开启关闭等待
     */
    @Value("${pool-thread.template-msg-send.wait-for-tasks-to-complete-on-shutdown:true}")
    private boolean waitForTasksToCompleteOnShutdown;
    /**
     * 启动初始化线程
     */
    @Value("${pool-thread.template-msg-send.pre-start-min-spare-threads:true}")
    protected boolean preStartMinSpareThreads;

    private ThreadPoolExecutor threadPoolExecutor;

    /**
     * 初始化线程池
     */
    @PostConstruct
    public void init() {
        threadPoolExecutor = ThreadPoolExecutorBuilder
                .getInstance(queueSize, namePrefix, coreSize, maxSize, keepAliveTime, isPreStartMinSpareThreads());
    }

    /**
     * 销毁
     */
    @PreDestroy
    public void destroy() {
        if (null != threadPoolExecutor) {
            threadPoolExecutor.shutdown(waitForTasksToCompleteOnShutdown, awaitTerminationSeconds);
            logger.info("关闭线程池{}", GrabInvoiceThreadPool.class.getSimpleName());
        }
    }

    /**
     * Executes the given command at some time in the future.
     *
     * @param command 带运行任务
     */
    public void execute(Runnable command) {
        threadPoolExecutor.execute(command);
    }

    public String getNamePrefix() {
        return namePrefix;
    }

    public void setNamePrefix(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    public Integer getCoreSize() {
        return coreSize;
    }

    public void setCoreSize(Integer coreSize) {
        this.coreSize = coreSize;
    }

    public Integer getMaxSize() {
        return maxSize;
    }

    public void setMaxSize(Integer maxSize) {
        this.maxSize = maxSize;
    }

    public Integer getQueueSize() {
        return queueSize;
    }

    public void setQueueSize(Integer queueSize) {
        this.queueSize = queueSize;
    }

    public Integer getKeepAliveTime() {
        return keepAliveTime;
    }

    public void setKeepAliveTime(Integer keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }

    public void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor) {
        this.threadPoolExecutor = threadPoolExecutor;
    }

    public boolean isPreStartMinSpareThreads() {
        return preStartMinSpareThreads;
    }

    public void setPreStartMinSpareThreads(boolean preStartMinSpareThreads) {
        this.preStartMinSpareThreads = preStartMinSpareThreads;
    }
}
