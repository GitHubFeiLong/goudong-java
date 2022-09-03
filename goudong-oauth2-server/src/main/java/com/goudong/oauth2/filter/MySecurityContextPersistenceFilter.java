package com.goudong.oauth2.filter;

import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.service.BaseUserService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 自定义填充 SecurityContextHolder
 * @author msi
 * @version 1.0
 * @date 2022/1/22 12:59
 */
public class MySecurityContextPersistenceFilter extends OncePerRequestFilter {
    //~fields
    //==================================================================================================================
    /**
     * 用户服务层
     */
    private final BaseUserService baseUserService;


    //~methods
    //==================================================================================================================
    public MySecurityContextPersistenceFilter(BaseUserService baseUserService) {
        this.baseUserService = baseUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 获取认证用户，并将其设置到 SecurityContext中
        try {
            if (httpServletRequest.getRequestURI().contains("/api/oauth2/authentication/refresh-token")) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
            BaseUserPO baseUserPO = baseUserService.getAuthentication(httpServletRequest);
            // 官网建议，避免跨多个线程的竞态条件
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            emptyContext.setAuthentication(baseUserPO);
            SecurityContextHolder.setContext(emptyContext);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

}