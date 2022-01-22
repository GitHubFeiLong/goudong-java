package com.goudong.oauth2.filter;

import com.goudong.oauth2.core.AuthenticationImpl;
import com.goudong.oauth2.service.BaseUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/22 12:59
 */
public class A extends OncePerRequestFilter {
    //~fields
    //==================================================================================================================
    /**
     * 用户服务层
     */
    private final BaseUserService baseUserService;


    //~methods
    //==================================================================================================================
    public A(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        AuthenticationImpl authentication = new AuthenticationImpl();
        authentication.setId(100L);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        SecurityContextHolder.clearContext();
    }


}