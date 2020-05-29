package com.cycs.poskp.util.threads;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * 	线程任务队列
 *
 * @see java.util.concurrent.LinkedBlockingQueue
 */
public class TaskQueue extends LinkedBlockingQueue<Runnable> {
    /**
     * 	序列化ID
     */
    private static final long serialVersionUID = 1L;
    /**
     * An {@link ExecutorService} that executes each submitted task using
     * one of possibly several pooled threads, normally configured
     * using {@link java.util.concurrent.Executors} factory methods.
     */
    private volatile ThreadPoolExecutor parent = null;

    // No need to be volatile. This is written and read in a single thread
    // (when stopping a context and firing the  listeners)
    private Integer forcedRemainingCapacity = null;

    /**
     * 	构造方法
     */
    public TaskQueue() {
        super();
    }

    /**
     * 	构造方法
     *
     * @param capacity 容量
     */
    public TaskQueue(int capacity) {
        super(capacity);
    }

    /**
     * 	构造方法
     *
     * @param c 线程容器
     */
    public TaskQueue(Collection<? extends Runnable> c) {
        super(c);
    }

    public void setParent(ThreadPoolExecutor tp) {
        parent = tp;
    }

    public void setForcedRemainingCapacity(Integer forcedRemainingCapacity) {
        this.forcedRemainingCapacity = forcedRemainingCapacity;
    }

    /**
     * 	强制线程 加入队列
     *
     * @param o 需加入队列的线程
     * @return boolean 是否加入
     */
    public boolean force(Runnable o) {
        if (parent == null || parent.isShutdown())
            throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
        return super.offer(o); //forces the item onto the queue, to be used if the task is rejected
    }

    /**
     * 	强制线程 加入队列
     *
     * @param o       需加入队列的线程
     * @param timeout 超时时间
     * @param unit    超时时间单位
     * @return boolean 是否加入
     * @throws InterruptedException {@link java.lang.InterruptedException}
     */
    public boolean force(Runnable o, long timeout, TimeUnit unit) throws InterruptedException {
        if (parent == null || parent.isShutdown())
            throw new RejectedExecutionException("Executor not running, can't force a command into the queue");
        return super.offer(o, timeout, unit); //forces the item onto the queue, to be used if the task is rejected
    }

    /**
     * (non-Javadoc)
     *
     * @see java.util.concurrent.LinkedBlockingQueue#offer(Object)
     * @see java.util.concurrent.ThreadPoolExecutor#execute(Runnable)
     */
    @Override
    public boolean offer(Runnable o) {
        //we can't do any checks
        //用户行为， 建议使用者必须初始化parent
        if (parent == null) return super.offer(o);
        //we are maxed out on threads, simply queue the object
        //当前线程数与最大的线程数相等， 加入队列
        if (parent.getPoolSize() == parent.getMaximumPoolSize()) return super.offer(o);
        //we have idle threads, just add it to the queue
        //待执行线程数 小于等于执行线程数，加入队列
        if (parent.getSubmittedCount() <= (parent.getPoolSize())) return super.offer(o);
        /*
         * if we have less threads than maximum force creation of a new thread
         * 执行线程数小于最大线程数 返回false 查看java.util.concurrent.ThreadPoolExecutor#execute(Runnable)如下：
         *  if (isRunning(c) && workQueue.offer(command)) {
         *    int recheck = ctl.get();
         *    if (! isRunning(recheck) && remove(command))
         *        reject(command);
         *    else if (workerCountOf(recheck) == 0)
         *        addWorker(null, false);
         * }
         * else if (!addWorker(command, false))
         *    reject(command);
         */
        if (parent.getPoolSize() < parent.getMaximumPoolSize()) return false;
        //if we reached here, we need to add it to the queue
        return super.offer(o);
    }

    /**
     * (non-Javadoc)
     *
     * @see java.util.concurrent.LinkedBlockingQueue#poll(long, TimeUnit)
     */
    @Override
    public Runnable poll(long timeout, TimeUnit unit)
            throws InterruptedException {
        Runnable runnable = super.poll(timeout, unit);
        if (runnable == null && parent != null) {
            // the poll timed out, it gives an opportunity to stop the current
            // thread if needed to avoid memory leaks.
            parent.stopCurrentThreadIfNeeded();
        }
        return runnable;
    }

    /**
     * (non-Javadoc)
     *
     * @see java.util.concurrent.LinkedBlockingQueue#take()
     */
    @Override
    public Runnable take() throws InterruptedException {
        if (parent != null && parent.currentThreadShouldBeStopped()) {
            return poll(parent.getKeepAliveTime(TimeUnit.MILLISECONDS),
                    TimeUnit.MILLISECONDS);
            // yes, this may return null (in case of timeout) which normally
            // does not occur with take()
            // but the ThreadPoolExecutor implementation allows this
        }
        return super.take();
    }

    /**
     * (non-Javadoc)
     *
     * @see java.util.concurrent.LinkedBlockingQueue#remainingCapacity()
     */
    @Override
    public int remainingCapacity() {
        if (forcedRemainingCapacity != null) {
            // ThreadPoolExecutor.setCorePoolSize checks that
            // remainingCapacity==0 to allow to interrupt idle threads
            // I don't see why, but this hack allows to conform to this
            // "requirement"
            return forcedRemainingCapacity.intValue();
        }
        return super.remainingCapacity();
    }
}
