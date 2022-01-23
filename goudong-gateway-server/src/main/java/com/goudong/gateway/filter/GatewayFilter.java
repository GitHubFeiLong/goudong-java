package com.goudong.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.openfeign.GoudongOauth2ServerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

/**
 * 网关过滤器
 * @Author msi
 * @Date 2021-04-08 14:06
 * @Version 1.0
 */
@Slf4j
@Component
public class GatewayFilter implements GlobalFilter, Ordered {

    /**
     * oauth2服务
     */
    private final GoudongOauth2ServerService goudongOauth2ServerService;

    public GatewayFilter(GoudongOauth2ServerService goudongOauth2ServerService) {
        this.goudongOauth2ServerService = goudongOauth2ServerService;
    }

    /**
     * 网管请求入口
     * @param exchange ServerWebExchange是一个HTTP请求-响应交互的契约。提供对HTTP请求和响应的访问，并公开额外的 服务器 端处理相关属性和特性，如请求属性。
     * @param chain
     * @return
     */
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().getPath();
        String method = request.getMethodValue();
        // 打印详细日志
        if (log.isDebugEnabled()) {
            log.debug("");
            log.debug("【{}】 {}", exchange.getRequest().getMethodValue(), exchange.getRequest().getURI());
            exchange.getRequest().getHeaders().entrySet().forEach(p->{
                log.debug("{} -> {}", p.getKey(), p.getValue());
            });
        }

        BaseUserDTO baseUserDTO = goudongOauth2ServerService.authorize(uri, method).getData();

        // 将token的md5加密后的值保存在token中
        ServerHttpRequest newRequest = request
                .mutate()
                .header(HttpHeaderConst.REQUEST_USER, URLEncoder.encode(JSON.toJSONString(baseUserDTO), "UTF-8"))
                .build();
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();
        return chain.filter(newExchange);
    }
    //
    //
    // /**
    //  * 检查当前请求是否能通过
    //  * @param request
    //  */
    // private String checkRequestAccess(ServerHttpRequest request) {
    //     String uri = request.getURI().getPath();
    //     HttpMethod method = request.getMethod();
    //
    //     /*
    //         白名单直接放行
    //      */
    //     List<BaseWhitelistDTO> listWhitelist = goudongOauth2ServerService.listWhitelist().getData();
    //
    //     // token
    //     String headerToken = request.getHeaders().getFirst(JwtTokenUtil.TOKEN_HEADER);
    //
    //
    //
    //     // 登录接口，直接放行
    //     boolean isLogin = (Objects.equals(uri, LOGIN_PASSWORD_API) && Objects.equals(method, HttpMethod.POST))
    //             || Objects.equals(uri, LOGIN_TOKEN_API) && Objects.equals(method, HttpMethod.POST);
    //     if (isLogin) {
    //         return GatewayFilter.DEFAULT_TOKEN_MD5_KEY;
    //     }
    //     // 欠佳
    //     // swagger文档，手动登录
    //     if (new AntPathMatcher().match(CommonConst.KNIFE4J_DOC_PATTERN, uri) && Objects.equals(method, HttpMethod.GET)) {
    //         // 还未登录swagger时，没有token
    //         if (headerToken == null) {
    //             return GatewayFilter.DEFAULT_TOKEN_MD5_KEY;
    //         }
    //         // 登陆过swagger了
    //         redisOperationsUtil.login(headerToken, getUserByToken(headerToken));
    //         return GatewayFilter.DEFAULT_TOKEN_MD5_KEY;
    //     }
    //
    //     // 白名单集合
    //     List<IgnoreResourceAntMatcher> ignoreList = redisOperationsUtil.getListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE, IgnoreResourceAntMatcher.class);
    //     // 检查本次请求是否是白名单能访问,直接return.
    //     if (IgnoreResourceAntMatcherUtil.checkAccess(uri, method, ignoreList)) {
    //         return GatewayFilter.DEFAULT_TOKEN_MD5_KEY;
    //     }
    //
    //     // 带上了token， 就需要判断Token是否有效
    //     if (StringUtils.isNotBlank(headerToken)) {
    //         String tokenMd5Key = JwtTokenUtil.generateRedisKey(headerToken);
    //         // 校验token是否有效
    //         userService.getTokenByTokenMd5(tokenMd5Key).getData().orElseThrow(()->ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED, "token无效"));
    //
    //         AuthorityUserDTO authorityUserDTO = getUserByToken(headerToken);
    //
    //         // 判断用户是否在线
    //         boolean offLine = !redisOperationsUtil.hasKey(RedisKeyEnum.OAUTH2_USER_INFO, tokenMd5Key);
    //         if (offLine) {
    //             // redis不存在token(未登录)
    //             throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
    //         }
    //
    //         String redisTokenValue = redisOperationsUtil.getStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, authorityUserDTO.getId());
    //
    //         // 判断在线token和本次携带的token是否相同。
    //         if (!Objects.equals(headerToken, redisTokenValue)) {
    //             // 已经有新token登录了，需要重新登录
    //             throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
    //         }
    //
    //         // 登录账户包含ADMIN角色,直接放行
    //         boolean hasRoleAdmin = authorityUserDTO.getAuthorityRoleDTOS().stream().filter(f->CommonConst.ROLE_ADMIN.equals(f.getRoleName())).count()>0;
    //         if (hasRoleAdmin) {
    //             // 延长用户在线时长
    //             redisOperationsUtil.renewLoginStatus(headerToken, authorityUserDTO.getId());
    //             return tokenMd5Key;
    //         }
    //
    //         // 当用户是正常登录访问的,那么检查权限是否足够
    //         List<AuthorityMenuDTO> authorityMenuDTOS = authorityUserDTO.getAuthorityMenuDTOS();
    //         List<IgnoreResourceAntMatcher> matcherList = IgnoreResourceAntMatcherUtil.menu2AntMatchers(authorityMenuDTOS);
    //
    //         // 断言能访问，当访问不了会进行异常
    //         IgnoreResourceAntMatcherUtil.assertAccess(uri, method, matcherList);
    //
    //         // 延长用户在线时长
    //         redisOperationsUtil.renewLoginStatus(headerToken, authorityUserDTO.getId());
    //         return tokenMd5Key;
    //     }
    //
    //
    //     String message = StringUtil.format("本次请求缺少请求头 {} ", JwtTokenUtil.TOKEN_HEADER);
    //     throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED, message);
    // }
    //
    // /**
    //  * 根据token获取用户
    //  * @param token
    //  * @return
    //  */
    // public AuthorityUserDTO getUserByToken(String token){
    //     log.info("token:{}", token);
    //     // 解析token,解析成功表示token有效
    //     // 当token是Basic开头时,authorityUserDTO只有username和password属性有值.
    //     AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);
    //     // base64格式
    //     if (authorityUserDTO.getId() == null) {
    //         // 查询用户
    //         AuthorityUserDTO userDetail = userService.getUserDetailByLoginName(authorityUserDTO.getUsername()).getData();
    //         if (userDetail == null) {
    //             throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
    //         }
    //         String rawPassword = authorityUserDTO.getPassword();
    //         String encodedPassword = userDetail.getPassword();
    //         // 使用 BCrypt 加密的方式进行匹配
    //         boolean matches = new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
    //         // 密码不正确，抛出异常
    //         if (!matches) {
    //             // 401 密码不匹配.
    //             throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
    //         }
    //
    //         return userDetail;
    //     }
    //
    //     return authorityUserDTO;
    // }

    @Override
    public int getOrder() {
        return 0;
    }
}
