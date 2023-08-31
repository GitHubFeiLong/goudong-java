package com.goudong.authentication.server.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.authentication.common.core.LoginResp;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import com.goudong.core.lang.Result;
import com.goudong.authentication.server.service.BaseUserRoleService;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.LoginDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 认证成功处理器
 *
 * @author msi
 * @date 2022/1/15 21:46
 * @version 1.0
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseUserManagerService baseUserManagerService;

    @Resource
    private ObjectMapper objectMapper;

    //~methods
    //==================================================================================================================
    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication 保存当前用户的登录信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        // 转换成自定义的Authentication对象
        MyAuthentication myAuthentication = (MyAuthentication) authentication;

        // 查询用户，角色，菜单
        LoginResp login = baseUserManagerService.login(myAuthentication);

        String json = objectMapper.writeValueAsString(Result.ofSuccess(login));

        httpServletResponse.getWriter().write(json);
    }

}
