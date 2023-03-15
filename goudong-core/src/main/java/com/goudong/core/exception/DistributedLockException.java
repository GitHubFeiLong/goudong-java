package com.goudong.core.exception;

/**
 * 类描述：
 * 分布式锁异常
 * @author cfl
 * @version 1.0
 * @date 2023/3/15 14:29
 */
public class DistributedLockException extends RuntimeException{
    public DistributedLockException() {
    }

    public DistributedLockException(String message) {
        super(message);
    }

    public DistributedLockException(String message, Throwable cause) {
        super(message, cause);
    }

    public DistributedLockException(Throwable cause) {
        super(cause);
    }

    public DistributedLockException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
