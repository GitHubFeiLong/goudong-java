package com.goudong.oauth2.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.exception.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.oauth2.dto.BaseAuthenticationLogDTO;
import com.goudong.oauth2.enumerate.AuthenticationLogTypeEnum;
import com.goudong.oauth2.service.BaseAuthenticationLogService;
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

    /**
     * 认证日志服务层接口
     */
    private final BaseAuthenticationLogService baseAuthenticationLogService;

    public AuthenticationFailureHandlerImpl(BaseAuthenticationLogService baseAuthenticationLogService) {
        this.baseAuthenticationLogService = baseAuthenticationLogService;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ClientExceptionEnum exceptionEnum = ClientExceptionEnum.UNAUTHORIZED;
        Result<BasicException> result = Result.ofFail(exceptionEnum);
        LogUtil.warn(log, "自定义登录失败处理器:{}", e.getMessage());
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

        // 保存认证日志
        BaseAuthenticationLogDTO baseAuthenticationLogDTO = new BaseAuthenticationLogDTO(
                (String)httpServletRequest.getAttribute("principal"),
                false,
                AuthenticationLogTypeEnum.SYSTEM.name().toLowerCase(),
                result.getClientMessage());
        baseAuthenticationLogService.create(baseAuthenticationLogDTO);

        httpServletResponse.setStatus(exceptionEnum.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String json = new ObjectMapper().writeValueAsString(result);
        httpServletResponse.getWriter().write(json);
    }
}
