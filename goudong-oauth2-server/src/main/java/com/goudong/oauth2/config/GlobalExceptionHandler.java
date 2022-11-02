package com.goudong.oauth2.config;

import com.goudong.boot.web.handler.HandlerInterface;
import com.goudong.core.lang.Result;
import com.goudong.oauth2.exception.AccountExpiredException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 类描述：
 * 认证权限全局异常补充
 * @author msi
 * @version 1.0
 * @date 2022/1/15 15:25
 */
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler implements HandlerInterface {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 账号失效
     * @param exception
     * @return
     */
    @ExceptionHandler(AccountExpiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Result<AuthenticationException> accountExpiredExceptionDispose(AccountExpiredException exception){
        // 打印错误日志
        log.error(LOG_ERROR_INFO,
                exception.getStatus(),
                exception.getCode(),
                exception.getClientMessage(),
                exception.getServerMessage(),
                exception.getMessage());

        // 堆栈跟踪
        printErrorMessage(log, "accountExpiredExceptionDispose", exception);

        return Result.ofFail(exception);
    }


}
