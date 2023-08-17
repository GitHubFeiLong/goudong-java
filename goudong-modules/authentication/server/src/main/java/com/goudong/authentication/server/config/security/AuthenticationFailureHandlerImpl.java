package com.goudong.authentication.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.core.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 认证失败处理器
 * @author msi
 * @date 2022/1/15 19:56
 * @version 1.0
 */
@Slf4j
@SuppressWarnings("Duplicates")
@Component
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {


    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ClientExceptionEnum exceptionEnum = ClientExceptionEnum.UNAUTHORIZED;
        Result<BasicException> result = Result.ofFail(exceptionEnum);
        log.warn("自定义登录失败处理器:{}", e.getMessage());
        // TODO是否需要返回200
        if (e instanceof UsernameNotFoundException) {
            // 为了更加安全，提示用户名或密码错误（小技巧）
            result.setClientMessage("用户名或密码错误");
        } else if (e instanceof BadCredentialsException) {
            // 为了更加安全，提示用户名或密码错误（小技巧）
            result.setClientMessage("用户名或密码错误");
        } else if (e instanceof AccountExpiredException) {
            result.setClientMessage(e.getMessage());
        } else {
            result.setClientMessage(e.getMessage());
        }


        httpServletResponse.setStatus(exceptionEnum.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String json = new ObjectMapper().writeValueAsString(result);
        httpServletResponse.getWriter().write(json);
    }
}
