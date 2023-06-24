package com.goudong.oauth2.filter;

import com.goudong.boot.web.core.BasicException;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.ListUtil;
import com.goudong.oauth2.enumerate.ExceptionEnum;
import com.goudong.oauth2.exception.AppException;
import com.goudong.oauth2.po.BaseAppPO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.repository.BaseAppRepository;
import com.goudong.oauth2.service.BaseUserService;
import com.goudong.oauth2.util.app.AppContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Resource;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * 类描述：
 * 自定义填充 SecurityContextHolder
 * @author msi
 * @version 1.0
 * @date 2022/1/22 12:59
 */
@Component
public class MySecurityContextPersistenceFilter extends OncePerRequestFilter {
    //~fields
    //==================================================================================================================
    private static List<String> IGNORE_URIS = ListUtil.newArrayList(
            "/**/authentication/login",
            "/**/authentication/refresh-token",
            "/**/*.html*",
            "/**/*.css*",
            "/**/*.js*",
            "/**/*.ico*",
            "/**/swagger-resources*",
            "/**/api-docs*",
            "/druid/**",
            "/actuator/**"
    );

    /**
     * 应用服务层
     */
    @Resource
    private BaseAppRepository baseAppRepository;

    /**
     * 用户服务层
     */
    @Lazy
    @Resource
    private BaseUserService baseUserService;

    //~methods
    //==================================================================================================================

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        // 获取认证用户，并将其设置到 SecurityContext中
        try {
            /*
                认证和刷新认证接口不需要添加上下文
             */
            // 先校验请求头应用Id
            String appId = httpServletRequest.getHeader(HttpHeaderConst.X_APP_ID);
            AssertUtil.isNotBlank(appId, () -> BasicException.client(String.format("请求头%s丢失", HttpHeaderConst.X_APP_ID)));
            // 获取appId
            AppContext appContext = new AppContext(appId);
            Long appIdLong = appContext.getAppId(appId);

            String requestURI = httpServletRequest.getRequestURI();
            // 本次请求是白名单，不需要进行后面的token校验
            AntPathMatcher antPathMatcher = new AntPathMatcher();
            Optional<String> first = IGNORE_URIS.stream()
                    .filter(f -> antPathMatcher.match(f, requestURI))
                    .findFirst();

            if (first.isPresent()) {
                BaseAppPO baseAppPO = baseAppRepository.findById(appIdLong)
                        .orElseThrow(() -> AppException
                                .builder(ExceptionEnum.X_APP_ID_INVALID)
                                .clientMessage("应用:{}无效")
                                .clientMessageParams(appId)
                                .build());
                AssertUtil.isTrue(baseAppPO.getStatus() == BaseAppPO.StatusEnum.PASS.getId(),
                        () -> AppException.builder(ExceptionEnum.X_APP_ID_INVALID).clientMessage("应用未通过").serverMessage("应用未通过审核").build());
                // 替换请求头中的应用Id
                httpServletRequest.setAttribute(HttpHeaderConst.X_APP_ID, baseAppPO.getId());

                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            // 替换请求头中的应用Id
            httpServletRequest.setAttribute(HttpHeaderConst.X_APP_ID, appIdLong);

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
