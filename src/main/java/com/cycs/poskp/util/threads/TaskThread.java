package com.cycs.poskp.util.threads;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工作线程
 *
 * @see java.lang.Thread
 */
public class TaskThread extends Thread {
    private static Logger logger = LoggerFactory.getLogger(TaskThread.class);
    /**
     * 创建时间
     */
    private final long creationTime;

    /**
     * 构造方法
     *
     * @param group  线程组
     * @param target 目标线程
     * @param name   线程名
     */
    public TaskThread(ThreadGroup group, Runnable target, String name) {
        super(group, new WrappingRunnable(target), name);
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * 构造方法
     *
     * @param group     线程组
     * @param target    目标线程
     * @param name      线程名
     * @param stackSize the desired stack size for the new thread, or zero to indicate that this parameter is to be ignored.
     */
    public TaskThread(ThreadGroup group, Runnable target, String name,
                      long stackSize) {
        super(group, new WrappingRunnable(target), name, stackSize);
        this.creationTime = System.currentTimeMillis();
    }

    /**
     * @return 线程创建时间
     */
    public final long getCreationTime() {
        return creationTime;
    }

    /**
     * Wraps a {@link Runnable} to swallow any {@link StopPooledThreadException}
     * instead of letting it go and potentially trigger a break in a debugger.
     */
    private static class WrappingRunnable implements Runnable {
        private Runnable wrappedRunnable;

        WrappingRunnable(Runnable wrappedRunnable) {
            this.wrappedRunnable = wrappedRunnable;
        }

        @Override
        public void run() {
            try {
                wrappedRunnable.run();
            } catch (StopPooledThreadException exc) {
                //expected : we just swallow the exception to avoid disturbing
                //debuggers like eclipse's
                logger.debug("Thread exiting on purpose", exc);
            }
        }

    }
}
