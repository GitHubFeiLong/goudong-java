package com.goudong.gateway.filter;

import com.goudong.commons.constant.CommonConst;
import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.openfeign.Oauth2Service;
import com.goudong.commons.pojo.IgnoreResourceAntMatcher;
import com.goudong.commons.utils.IgnoreResourceAntMatcherUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 网关过滤器
 * @Author msi
 * @Date 2021-04-08 14:06
 * @Version 1.0
 */
@Slf4j
@Component
public class GatewayFilter implements GlobalFilter, Ordered {

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

    @Resource
    private Oauth2Service oauth2Service;

    /**
     * 网管请求入口
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入网关过滤器");
        ServerHttpRequest request = exchange.getRequest();
        // 检查请求是否能放入
        checkRequestAccess(request);
        return chain.filter(exchange);
    }


    /**
     * 检查当前请求是否能通过
     * @param request
     */
    private void checkRequestAccess(ServerHttpRequest request) {
        // token
        String headerToken = request.getHeaders().getFirst(JwtTokenUtil.TOKEN_HEADER);
        String uri = request.getURI().getPath();
        HttpMethod method = request.getMethod();
        // 登录接口，直接放行
        boolean isLogin = Objects.equals(uri, "/api/oauth2/user/login") && Objects.equals(method, HttpMethod.POST);
        if (isLogin) {
            return;
        }
        // swagger文档，手动登录
        if (new AntPathMatcher().match(CommonConst.KNIFE4J_DOC_PATTERN, uri) && Objects.equals(method, HttpMethod.GET)) {
            // 还未登录swagger时，没有token
            if (headerToken == null) {
                return;
            }
            // 登陆过swagger了
            redisOperationsUtil.login(headerToken, getUserByToken(headerToken));
            return;
        }

        // 白名单集合
        List<IgnoreResourceAntMatcher> ignoreList = redisOperationsUtil.getListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE, IgnoreResourceAntMatcher.class);
        // 检查本次请求是否是白名单能访问,直接return.
        if (IgnoreResourceAntMatcherUtil.checkAccess(uri, method, ignoreList)) {
            return;
        }

        // 带上了token， 就需要判断Token是否有效
        if (StringUtils.isNotBlank(headerToken)) {
            AuthorityUserDTO authorityUserDTO = getUserByToken(headerToken);

            // 判断用户是否在线
            String tokenMd5Key = JwtTokenUtil.generateRedisKey(headerToken);
            boolean offLine = !redisOperationsUtil.hasKey(RedisKeyEnum.OAUTH2_USER_INFO, tokenMd5Key);
            if (offLine) {
                // redis不存在token(未登录)
                throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
            }

            String redisTokenValue = redisOperationsUtil.getStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, authorityUserDTO.getId());

            // 判断在线token和本次携带的token是否相同。
            if (!Objects.equals(headerToken, redisTokenValue)) {
                // 已经有新token登录了，需要重新登录
                throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
            }

            // 登录账户包含ADMIN角色,直接放行
            boolean hasRoleAdmin = authorityUserDTO.getAuthorityRoleDTOS().stream().filter(f->CommonConst.ROLE_ADMIN.equals(f.getRoleName())).count()>0;
            if (hasRoleAdmin) {
                // 延长用户在线时长
                redisOperationsUtil.renewLoginStatus(headerToken, authorityUserDTO.getId());
                return;
            }

            // 当用户是正常登录访问的,那么检查权限是否足够
            List<AuthorityMenuDTO> authorityMenuDTOS = authorityUserDTO.getAuthorityMenuDTOS();
            List<IgnoreResourceAntMatcher> matcherList = IgnoreResourceAntMatcherUtil.menu2AntMatchers(authorityMenuDTOS);

            // 断言能访问，当访问不了会进行异常
            IgnoreResourceAntMatcherUtil.assertAccess(uri, method, matcherList);

            // 延长用户在线时长
            redisOperationsUtil.renewLoginStatus(headerToken, authorityUserDTO.getId());
        }

        throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
    }

    /**
     * 根据token获取用户
     * @param token
     * @return
     */
    public AuthorityUserDTO getUserByToken(String token){
        log.info("token:{}", token);
        // 解析token,解析成功表示token有效
        // 当token是Basic开头时,authorityUserDTO只有username和password属性有值.
        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);
        // base64格式
        if (authorityUserDTO.getId() == null) {
            // 查询用户
            AuthorityUserDTO userDetail = oauth2Service.getUserDetailByLoginName(authorityUserDTO.getUsername()).getData();
            String rawPassword = authorityUserDTO.getPassword();
            String encodedPassword = userDetail.getPassword();
            // 使用 BCrypt 加密的方式进行匹配
            boolean matches = new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
            // 密码不正确，抛出异常
            if (!matches) {
                // 404 为匹配.
                throw ClientException.clientException(ClientExceptionEnum.NAME_OR_PWD_ERROR);
            }

            return userDetail;
        }

        return authorityUserDTO;
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
