package com.goudong.gateway.filter;

import com.goudong.commons.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 网关过滤器
 * @Author msi
 * @Date 2021-04-08 14:06
 * @Version 1.0
 */
@Slf4j
@Component
public class GatewayFilter implements GlobalFilter, Ordered {


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入网关过滤器");

        ServerHttpRequest request = exchange.getRequest();
        String headerToken = request.getHeaders().getFirst(JwtTokenUtil.TOKEN_HEADER);

        // 只要带上了token， 就需要判断Token是否有效
        if (!StringUtils.isEmpty(headerToken)) {
            log.info("headerToken:{}", headerToken);
//            JwtTokenUtil.resolveToken(headerToken);
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
