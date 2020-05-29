package com.cycs.poskp.util.threads;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.tomcat.util.security.PrivilegedSetTccl;

/**
 * An object that creates new threads on demand.  Using thread factories
 * removes hardwiring of calls to {@link Thread#Thread(Runnable) new Thread},
 * enabling applications to use special thread subclasses, priorities, etc.
 *
 * <p>1.指定线程组</p>
 * <p>2.指定线程名</p>
 * <p>3.指定线程优先级</p>
 *
 * @see java.util.concurrent.ThreadFactory
 */
public class TaskThreadFactory implements ThreadFactory {
    /**
     * A thread group represents a set of threads. In addition, a thread
     * group can also include other thread groups. The thread groups form
     * a tree in which every thread group except the initial thread group
     * has a parent.
     */
    private final ThreadGroup group;
    /**
     *	 初始化一个线程计数器 作为线程名后缀
     */
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    /**
     * 	线程名前缀
     */
    private final String namePrefix;
    /**
     * Marks this thread as either a {@linkplain Thread#isDaemon daemon} thread
     * or a user thread. The Java Virtual Machine exits when the only
     * threads running are all daemon threads.
     */
    private final boolean daemon;
    /**
     * 	指定线程的优先级
     */
    private final int threadPriority;

    /**
     * 	构造方法
     *
     * @param namePrefix 线程名前缀
     * @param daemon     守护线程
     * @param priority   优先级
     */
    public TaskThreadFactory(String namePrefix, boolean daemon, int priority) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.namePrefix = namePrefix;
        this.daemon = daemon;
        this.threadPriority = priority;
    }

    /**
     * (non-Javadoc)
     *
     * @see java.util.concurrent.ThreadFactory#newThread(Runnable)
     */
    @Override
    public Thread newThread(Runnable r) {
        TaskThread t = new TaskThread(group, r, namePrefix + threadNumber.getAndIncrement());
        t.setDaemon(daemon);
        t.setPriority(threadPriority);

        // Set the context class loader of newly created threads to be the class
        // loader that loaded this factory. This avoids retaining references to
        // web application class loaders and similar.
        if (Constants.IS_SECURITY_ENABLED) {
            PrivilegedAction<Void> pa = new PrivilegedSetTccl(
                    t, getClass().getClassLoader());
            AccessController.doPrivileged(pa);
        } else {
            t.setContextClassLoader(getClass().getClassLoader());
        }

        return t;
    }
}
