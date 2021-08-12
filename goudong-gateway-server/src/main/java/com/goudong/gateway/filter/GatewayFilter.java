package com.goudong.gateway.filter;

import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
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
        String headerToken = request.getHeaders().getFirst(JwtTokenUtil.TOKEN_HEADER);
        // 只要带上了token， 就需要判断Token是否有效
        if (!StringUtils.isEmpty(headerToken)) {
            log.info("headerToken:{}", headerToken);
            // 解析token,解析成功表示token有效
            AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(headerToken);
            // 根据用户uuid,判断redis 是否还存储该token(判断是否在线)
            String stringValue = redisOperationsUtil.getStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, authorityUserDTO.getUuid());
            // token不相同
            if (stringValue == null || !stringValue.equals(headerToken)) {
                // redis不存在token(未登录)或者该token失效(已经有新token登录了) <重新登录>
                Result.ofFail(ClientException.clientException(ClientExceptionEnum.UNAUTHORIZED));
            }
            // 当用户是正常登录访问的,那么检查权限是否足够
            List<AuthorityMenuDTO> menuDTOList = redisOperationsUtil.getListValue(RedisKeyEnum.OAUTH2_USER_MENU, authorityUserDTO.getUuid());

//            menuDTOList.stream()
//                    .filter(f-> )
//                    .forEach();

        }
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
