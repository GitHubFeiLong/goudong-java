package com.goudong.oauth2.config;

import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;


/**
 * 类描述：
 * 自定义身份验证详细信息源
 * @ClassName CustomAuthenticationDetailsSource
 * @Author msi
 * @Date 2021-05-02 10:40
 * @Version 1.0
 */
@Component
public class CustomAuthenticationDetailsSource implements AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> {

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest httpServletRequest) {

        return new CustomWebAuthenticationDetails(httpServletRequest);
    }
}
