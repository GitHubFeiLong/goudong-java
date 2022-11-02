package com.goudong.boot.web.handler;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.core.lang.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 类描述：
 * spring security 的异常处理
 * @author cfl
 * @version 1.0
 * @date 2022/11/1 16:43
 */
@Order(10)
@RestControllerAdvice
public class AccessDeniedExceptionHandler implements HandlerInterface{

    private static final Logger log = LoggerFactory.getLogger(AccessDeniedExceptionHandler.class);

    /**
     * 自定义403异常
     * 使用自定义的AccessDeniedHandler时，当方法加注解没权限时不起作用，所以使用全局异常
     * @param exception
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Result accessDeniedExceptionDispose(AccessDeniedException exception) {
        BasicException basicException = ClientExceptionEnum.FORBIDDEN.client().serverMessage(exception.getMessage());
        // 打印错误日志
        log.error(LOG_ERROR_INFO,
                basicException.getStatus(),
                basicException.getCode(),
                basicException.getClientMessage(),
                basicException.getServerMessage(),
                exception.getMessage());

        // 堆栈跟踪
        printErrorMessage(log, "accessDeniedExceptionDispose", exception);

        return Result.ofFail(basicException);
    }

}
