package com.goudong.oauth2.config;

import com.goudong.commons.exception.user.AccountExpiredException;
import com.goudong.commons.frame.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/15 15:25
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends com.goudong.commons.exception.GlobalExceptionHandler{
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    public GlobalExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    /**
     * 全局处理自定义异常
     * @param exception
     * @return
     */
    @ExceptionHandler(AccountExpiredException.class)
    public Result<AuthenticationException> authenticationExceptionResult(AccountExpiredException exception){
        // 设置响应码
        response.setStatus(401);
        // 堆栈跟踪
        super.printErrorMessage("exception", exception);

        return Result.ofFail(exception);
    }

}