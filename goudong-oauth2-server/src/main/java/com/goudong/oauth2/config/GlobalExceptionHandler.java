package com.goudong.oauth2.config;

import com.goudong.commons.exception.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.user.AccountExpiredException;
import com.goudong.commons.framework.core.Result;
import com.goudong.oauth2.config.security.AccessDeniedHandlerImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * 扩充全局异常
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
    public Result<AuthenticationException> authenticationException(AccountExpiredException exception){
        // 设置响应码
        response.setStatus(401);
        // 堆栈跟踪
        super.printErrorMessage("exception", exception);

        return Result.ofFail(exception);
    }

    /**
     * 自定义403异常
     * 使用自定义的AccessDeniedHandler时，当方法加注解没权限时不起作用，所以使用全局异常
     * @see AccessDeniedHandlerImpl
     * @param exception
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result accessDeniedException(AccessDeniedException exception) {
        ClientExceptionEnum notAuthorization = ClientExceptionEnum.FORBIDDEN;
        // 设置响应码
        response.setStatus(notAuthorization.getStatus());
        // 堆栈跟踪
        super.printErrorMessage("accessDeniedException", exception);

        return Result.ofFailByForBidden("权限不足", exception.getMessage());
    }

}