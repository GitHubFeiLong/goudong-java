package com.goudong.gateway.filter;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.digest.MD5;
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
        // 过滤路径直接放行
        // 端口后的请求地址
        String uri = request.getURI().getPath();
        HttpMethod method = request.getMethod();
        // 白名单集合
        List<IgnoreResourceAntMatcher> ignoreList = redisOperationsUtil.getListValue(RedisKeyEnum.OAUTH2_IGNORE_RESOURCE, IgnoreResourceAntMatcher.class);
        // 抛出异常/返回false都是没有权限(或者白名单没有配置完善)
        boolean access = IgnoreResourceAntMatcherUtil.checkAccess(uri, method, ignoreList);
        // 能访问,直接return.
        if (access) {
            return;
        }

        // 不是白名单的就需要权限验证了.
        String headerToken = request.getHeaders().getFirst(JwtTokenUtil.TOKEN_HEADER);
        // 只要带上了token， 就需要判断Token是否有效
        if (StringUtils.isNotBlank(headerToken)) {
            log.info("headerToken:{}", headerToken);
            // 解析token,解析成功表示token有效
            // 当token是Basic开头时,authorityUserDTO只有username和password属性有值.
            AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(headerToken);

            boolean isBasicType = authorityUserDTO.getId() == null;
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

                // 登录账户是ADMIN,直接放行
                boolean hasRoleAdmin = authorityUserDTO.getAuthorityRoleDTOS().stream().filter(f->CommonConst.ROLE_ADMIN.equals(f.getRoleName())).count()>0;
                if (hasRoleAdmin) {
                    return;
                }
                // basic方式（swagger）不需要验证单一登录
                authorityUserDTO = userDetail;
            }

            // 延时
            redisOperationsUtil.login(headerToken, authorityUserDTO);

            // 根据用户id,判断redis 是否还存储该token(判断是否在线)
            String stringValue = redisOperationsUtil.getStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, authorityUserDTO.getId().toString());
            // 正常业务系统访问，未登录或者与登录的token值不同<重新登录>
            if ((!isBasicType && stringValue == null) || !stringValue.equals(headerToken)) {
                // redis不存在token(未登录)或者该token失效(已经有新token登录了)
                throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
            }

            // 当用户是正常登录访问的,那么检查权限是否足够
            List<AuthorityMenuDTO> authorityMenuDTOS = authorityUserDTO.getAuthorityMenuDTOS();
            List<IgnoreResourceAntMatcher> matcherList = IgnoreResourceAntMatcherUtil.menu2AntMatchers(authorityMenuDTOS);
            // 断言能访问，当访问不了会进行异常
            IgnoreResourceAntMatcherUtil.assertAccess(uri, method, matcherList);
            return;
        }

        throw ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
