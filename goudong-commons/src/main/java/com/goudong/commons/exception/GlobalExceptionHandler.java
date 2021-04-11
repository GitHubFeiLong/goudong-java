package com.goudong.commons.exception;


import com.goudong.commons.pojo.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局捕获异常
 * 1.捕获返回json格式
 * 2.捕获返回页面
 * @ClassName GlobalExceptionHandler
 * @Author msi
 * @Date 2019/7/28 21:51
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.zuopei")
public class GlobalExceptionHandler {
    /**
     * 错误日志模板
     */
    public static final String LOG_ERROR_INFO = "http响应码：{}，错误代码：{}，客户端错误信息：{}，服务端错误信息：{}";


    /**
     * 响应对象
     */
    @Resource
    private HttpServletResponse response;


    /**
     * 全局处理客户端错误
     * @param clientException      客户端异常
     * @return
     */
    @ExceptionHandler(BasicException.ClientException.class)
    public Result<BasicException.ClientException> clientExceptionDispose(BasicException.ClientException clientException){
        // 设置响应码
        response.setStatus(clientException.getStatus());
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, clientException.getStatus(), clientException.getCode(), clientException.getClientMessage(), clientException.getServerMessage());
        // 堆栈跟踪
        clientException.printStackTrace();

        return Result.ofFail(clientException);
    }

    /**
     * 全局处理服务端错误
     * @param serverException      服务端异常
     * @return
     */
    @ExceptionHandler(BasicException.ServerException.class)
    public Result<BasicException.ServerException> serveExceptionDispose(BasicException.ServerException serverException){
        // 设置响应码
        response.setStatus(serverException.getStatus());

        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, serverException.getStatus(), serverException.getCode(), serverException.getClientMessage(), serverException.getServerMessage());
        // 堆栈跟踪
        serverException.printStackTrace();

        return Result.ofFail(serverException);
    }

    /**
     * 捕获意料之外的异常Exception
     * @param e
     * @return
     */
    @ExceptionHandler(Throwable.class)
    public Result<Throwable> otherErrorDispose(Throwable e){
        this.response.setStatus(500);
        // 打印错误日志
        log.error(GlobalExceptionHandler.LOG_ERROR_INFO, 500, "ERROR", "未知异常", e.getMessage());
        // 堆栈跟踪
        e.printStackTrace();

        return Result.ofFail(e);
    }

}
