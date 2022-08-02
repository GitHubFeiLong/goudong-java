package com.goudong.bpm.filter;

import com.goudong.commons.core.context.UserContext;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.core.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import javax.servlet.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 因为activiti集成了security，所以这里我们需要使用这个拦截器，将网关传递的用户信息填充到spring security上下文中
 *extends OncePerRequestFilter
 *
 * @author cfl
 * @version 1.0
 * @date 2022/7/31 23:11
 */
@Slf4j
public class BpmAuthenticationFilter implements Filter {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            BaseUserDTO user = UserContext.get();
            // id=0，表示是匿名用户。
            //if (user == null || user.getId() == 0) {
            if (user == null && user.getId() == 0) {
                LogUtil.warn(log, "本次请求未认证");
                throw new ClientException(ClientExceptionEnum.UNAUTHORIZED);
            }

            LogUtil.info(log, "请求用户：{}", user);

            SecurityContextHolder.setContext(
                    new SecurityContextImpl(
                            new Authentication() {
                                @Override
                                public Collection<? extends GrantedAuthority> getAuthorities() {
                                    Collection<SimpleGrantedAuthority> roles = user.getRoles() == null ? new ArrayList() : user.getRoles()
                                            .stream()
                                            .map(role -> new SimpleGrantedAuthority(role.getRoleName()))
                                            .collect(Collectors.toList());

                                    // 给用户添加默认流程角色，使其能正常使用：processRuntime，taskRuntime
                                    roles.add( new SimpleGrantedAuthority("ROLE_ACTIVITI_USER"));

                                    return roles;
                                }
                                @Override
                                public Object getCredentials() {
                                    return user.getPassword();
                                }
                                @Override
                                public Object getDetails() {
                                    return user;
                                }
                                @Override
                                public Object getPrincipal() {
                                    return user;
                                }
                                @Override
                                public boolean isAuthenticated() {
                                    return true;
                                }
                                @Override
                                public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException { }
                                @Override
                                public String getName() {
                                    return user.getUsername();
                                }
                            }));
            // 设置当前用户的负责人信息（使用用户名）
            org.activiti.engine.impl.identity.Authentication.setAuthenticatedUserId(user.getUsername());

            // 接着执行余下的Filter
            chain.doFilter(request, response);
        } finally {
            // 清理
            SecurityContextHolder.clearContext();
        }
    }
}
