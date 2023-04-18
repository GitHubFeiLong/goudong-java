package com.goudong.gateway.context;

import com.goudong.boot.web.core.ApiLog;

/**
 * 类描述：
 * 获取当前请求的日志信息
 * @author cfl
 * @date 2023/4/18 9:28
 * @version 1.0
 */
public final class LogContext {

    //~fields
    //==================================================================================================================
    private static final ThreadLocal<ApiLog> threadLocal = new ThreadLocal<>();

    //~methods
    //==================================================================================================================
    private LogContext() {
    }

    /**
     * 设置当前请求的用户信息
     * @param user
     */
    public static void set(ApiLog apiLog) {
        threadLocal.set(apiLog);
    }

    /**
     * 获取当前请求的用户信息
     * @return
     */
    public static ApiLog get() {
        return threadLocal.get();
    }

    /**
     * 处理完请求，清除内容
     */
    public static void remove() {
        threadLocal.remove();
    }
}
