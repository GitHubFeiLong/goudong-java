package com.zhy.authentication.server.config.security;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.ListUtil;
import com.zhy.authentication.common.core.Jwt;
import com.zhy.authentication.common.core.UserToken;
import com.zhy.authentication.server.constant.HttpHeaderConst;
import com.zhy.authentication.server.repository.BaseAppRepository;
import com.zhy.authentication.server.service.BaseAppService;
import com.zhy.authentication.server.service.BaseUserService;
import com.zhy.authentication.server.service.dto.BaseAppDTO;
import com.zhy.authentication.server.service.dto.MyAuthentication;
import io.jsonwebtoken.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 自定义填充 SecurityContextHolder
 * @author msi
 * @version 1.0
 * @date 2022/1/22 12:59
 */
@Component
@Slf4j
public class MySecurityContextPersistenceFilter extends OncePerRequestFilter {
    //~fields
    //==================================================================================================================
    private static List<String> STATIC_URIS = ListUtil.newArrayList(
            "/**/*.html*",
            "/**/*.css*",
            "/**/*.js*",
            "/**/*.ico*",
            "/**/swagger-resources*",
            "/**/api-docs*",
            "/druid/**",
            "/actuator/**"
    );
    private static List<String> IGNORE_URIS = ListUtil.newArrayList(
            "/**/user/login",
            "/**/drop-down/base-app/all-drop-down" // 应用下拉
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

    @Resource
    private BaseAppService baseAppService;

    //~methods
    //==================================================================================================================

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = httpServletRequest.getRequestURI();
        // 本次请求是白名单，不需要进行后面的token校验
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        boolean staticUri = STATIC_URIS.stream()
                .filter(f -> antPathMatcher.match(f, requestURI))
                .findFirst().isPresent();
        if (staticUri) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        Long appId = getAppId(httpServletRequest);
        // 设置应用id到请求属性中，供后续使用
        httpServletRequest.setAttribute(HttpHeaderConst.X_APP_ID, appId);
        // 获取认证用户，并将其设置到 SecurityContext中
        try {
            Optional<String> first = IGNORE_URIS.stream()
                    .filter(f -> antPathMatcher.match(f, requestURI))
                    .findFirst();
            if (first.isPresent()) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }

            BaseAppDTO appDTO = baseAppService.findOne(appId).orElseThrow(() -> ClientException
                    .builder()
                    .clientMessageTemplate("X-App-Id:{}无效")
                    .clientMessageParams(appId)
                    .build());
            AssertUtil.isTrue(appDTO.getEnabled(), () -> ClientException
                    .builder()
                    .clientMessageTemplate("X-App-Id:{}未激活")
                    .clientMessageParams(appId)
                    .build());

            String authorization = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            AssertUtil.isNotBlank(authorization, () -> ClientException.clientByUnauthorized());
            String prefix = "Bearer ";
            AssertUtil.isTrue(authorization.startsWith(prefix), () -> ClientException.client("请求头" + HttpHeaders.AUTHORIZATION + "格式错误"));
            String token = authorization.substring(prefix.length());

            Jwt jwt = new Jwt(0, TimeUnit.SECONDS, appDTO.getSecret());
            UserToken userToken = jwt.parseToken(token);
            log.debug("解析token：{}", userToken);

            MyAuthentication myAuthentication = new MyAuthentication();
            myAuthentication.setId(userToken.getId());
            myAuthentication.setAppId(userToken.getAppId());
            myAuthentication.setRealAppId(userToken.getRealAppId());
            myAuthentication.setUsername(userToken.getUsername());
            myAuthentication.setRoles(userToken.getRoles().stream().map(m -> new SimpleGrantedAuthority(m)).collect(Collectors.toList()));
            // 官网建议，避免跨多个线程的竞态条件
            SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
            emptyContext.setAuthentication(myAuthentication);
            SecurityContextHolder.setContext(emptyContext);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } finally {
            SecurityContextHolder.clearContext();
        }
    }

    /**
     * 获取请求头的appId
     * @param httpServletRequest
     * @return
     */
    private Long getAppId(HttpServletRequest httpServletRequest) {
        // 先校验请求头应用Id
        String appIdStr = httpServletRequest.getHeader(HttpHeaderConst.X_APP_ID);
        AssertUtil.isNotBlank(appIdStr, () -> BasicException.client(String.format("请求头%s丢失", HttpHeaderConst.X_APP_ID)));
        try {
            return Long.parseLong(appIdStr);
        } catch (NumberFormatException e) {
            throw BasicException.client(String.format("请求头%s=%s无效", HttpHeaderConst.X_APP_ID, appIdStr));
        }
    }

}
