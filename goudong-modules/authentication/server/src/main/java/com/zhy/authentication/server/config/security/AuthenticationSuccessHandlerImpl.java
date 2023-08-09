package com.zhy.authentication.server.config.security;

import cn.hutool.core.bean.BeanUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.core.lang.Result;
import com.zhy.authentication.server.domain.BaseRole;
import com.zhy.authentication.server.domain.BaseUserRole;
import com.zhy.authentication.server.service.BaseRoleService;
import com.zhy.authentication.server.service.BaseUserRoleService;
import com.zhy.authentication.server.service.BaseUserService;
import com.zhy.authentication.server.service.dto.BaseRoleDTO;
import com.zhy.authentication.server.service.dto.BaseUserDTO;
import com.zhy.authentication.server.service.dto.LoginDTO;
import com.zhy.authentication.server.service.dto.MyAuthentication;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * 用户服务
     */
    @Lazy
    @Resource
    private BaseUserService baseUserService;

    @Resource
    private ObjectMapper objectMapper;

    @Resource
    private BaseUserRoleService baseUserRoleService;

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
        LoginDTO login = baseUserService.login(myAuthentication);

        String json = objectMapper.writeValueAsString(Result.ofSuccess(login));

        httpServletResponse.getWriter().write(json);
    }

}
