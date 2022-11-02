package com.goudong.boot.web.handler;

import org.slf4j.Logger;

/**
 * 接口描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/11/2 16:53
 */
public interface HandlerInterface {
    /**
     * 错误日志模板
     */
    String LOG_ERROR_INFO = "http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}，扩展信息：{}";

    /**
     * 打印日志
     * @param exceptionHandlerMethod 异常处理的方法
     * @param exception 异常对象
     */
    default void printErrorMessage(Logger log, String exceptionHandlerMethod, Throwable exception) {
        // 开启debug就打印堆栈
        if (log.isDebugEnabled()) {
            exception.printStackTrace();
        } else {
            log.error("程序捕获全局异常的方法-{}，错误信息：{}", exceptionHandlerMethod, exception);
        }
    }
}
