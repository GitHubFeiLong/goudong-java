package com.goudong.oauth2.config.security;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 认证时详细信息配置（除了用户名和密码，还可以自定义配置额外属性）
 * @see WebAuthenticationDetailsImpl
 * @author msi
 * @date 2022/1/15 20:20
 * @version 1.0
 */
@Component
public class AuthenticationDetailsSourceImpl implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest httpServletRequest) {

        return new WebAuthenticationDetailsImpl(httpServletRequest);
    }
}
