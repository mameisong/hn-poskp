package com.cycs.poskp.util.threads;

/**
 * 	线程停止 自定义异常
 */
public class StopPooledThreadException extends RuntimeException {
    /**
     * 	序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 	构造方法
     *
     * @param msg 错误消息
     */
    public StopPooledThreadException(String msg) {
        super(msg);
    }
}
