package com.cycs.poskp.util.threads;

/**
 * 	静态常量
 */
public final class Constants {
    /**
     * 	默认更新延迟时间
     */
    public static final long DEFAULT_THREAD_RENEWAL_DELAY = 1000L;

    /**
     * 	系统是否打开安全管理器
     */
    public static final boolean IS_SECURITY_ENABLED = (System.getSecurityManager() != null);

    /**
     * Stopping thread [{0}] to avoid potential memory leaks after a context was stopped.
     */
    public static final String STOP_MSG = "停止线程[%s]，避免上下文停止后的内存泄漏";
}
