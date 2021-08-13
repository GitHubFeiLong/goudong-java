package com.goudong.gateway.filter;

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
        // 过滤路径直接放行
        String url = request.getURI().toString();
        HttpMethod method = request.getMethod();
        List<IgnoreResourceAntMatcher> ignoreList = redisOperationsUtil.getListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE);
        // 抛出异常/返回false都是没有权限(或者白名单没有配置完善)
        boolean access = IgnoreResourceAntMatcherUtil.checkAccess(url, method, ignoreList);
        // 能访问,直接return.
        if (access) {
            return;
        }

        // 不是白名单的就需要权限验证了.
        String headerToken = request.getHeaders().getFirst(JwtTokenUtil.TOKEN_HEADER);
        // 只要带上了token， 就需要判断Token是否有效
        if (!StringUtils.isEmpty(headerToken)) {
            log.info("headerToken:{}", headerToken);
            // 解析token,解析成功表示token有效
            // 当token是Basic开头时,authorityUserDTO只有username和password属性有值.
            AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(headerToken);

            boolean isBasicType = authorityUserDTO.getUuid() == null;
            if (isBasicType) {
                // 查询用户
                AuthorityUserDTO userDetail = oauth2Service.getUserDetailByLoginName(authorityUserDTO.getLoginName()).getData();
                String rawPassword = authorityUserDTO.getPassword();
                String encodedPassword = userDetail.getPassword();
                // 使用 BCrypt 加密的方式进行匹配
                boolean matches = new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
                // 密码不正确，抛出异常
                if (!matches) {
                    // 404 为匹配.
                    throw ClientException.clientException(ClientExceptionEnum.NOT_FOUND);
                }

                authorityUserDTO = userDetail;

                // 判断是否存在key
                boolean b = redisOperationsUtil.hasKey(RedisKeyEnum.OAUTH2_USER_IGNORE_RESOURCE, userDetail.getUuid());
                if (!b) {
                    List<IgnoreResourceAntMatcher> ignoreResourceAntMatchers = IgnoreResourceAntMatcherUtil.menu2AntMatchers(userDetail.getAuthorityMenuDTOS());
                    redisOperationsUtil.setListValue(RedisKeyEnum.OAUTH2_USER_IGNORE_RESOURCE, ignoreResourceAntMatchers, userDetail.getUuid());
                }
            }

            // 根据用户uuid,判断redis 是否还存储该token(判断是否在线)
            String stringValue = redisOperationsUtil.getStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, authorityUserDTO.getUuid());
            // token不相同
            if (stringValue == null || !stringValue.equals(headerToken)) {
                // redis不存在token(未登录)或者该token失效(已经有新token登录了) <重新登录>
                throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
            }

            // 当用户是正常登录访问的,那么检查权限是否足够
            List<AuthorityMenuDTO> authorityMenuDTOS = authorityUserDTO.getAuthorityMenuDTOS();


        }

        throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
