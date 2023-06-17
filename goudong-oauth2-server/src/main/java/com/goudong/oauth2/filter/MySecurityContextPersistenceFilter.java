package com.goudong.oauth2.filter;

import com.goudong.boot.web.core.ClientException;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.core.util.AssertUtil;
import com.goudong.oauth2.po.BaseAppPO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.service.BaseAppService;
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
     * 应用服务层
     */
    private final BaseAppService baseAppService;

    /**
     * 用户服务层
     */
    private final BaseUserService baseUserService;

    //~methods
    //==================================================================================================================
    public MySecurityContextPersistenceFilter(BaseAppService baseAppService,
                                              BaseUserService baseUserService) {
        this.baseAppService = baseAppService;
        this.baseUserService = baseUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 获取认证用户，并将其设置到 SecurityContext中
        try {
            // 先校验请求头应用Id
            String appId = httpServletRequest.getHeader(HttpHeaderConst.X_APP_ID);
            AssertUtil.isNotBlank(appId, () -> new IllegalArgumentException(String.format("请求头%s丢失", HttpHeaderConst.X_APP_ID)));
            BaseAppPO baseAppPO = baseAppService.getByAppId(appId);
            AssertUtil.isTrue(baseAppPO.getStatus() == BaseAppPO.StatusEnum.PASS.getId(), () -> ClientException.client("应用不可用"));
            // 替换请求头中的应用Id
            httpServletRequest.setAttribute(HttpHeaderConst.X_APP_ID, baseAppPO.getId());

            /*
                认证和刷新认证接口不需要添加上下文
             */
            String requestURI = httpServletRequest.getRequestURI();
            if (requestURI.contains("/authentication/refresh-token")
                    ||requestURI.contains("/authentication/login")) {
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