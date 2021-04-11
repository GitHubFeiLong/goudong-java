package com.goudong.gateway.filter;

import com.goudong.commons.pojo.Result;
import com.goudong.gateway.service.Oauth2Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
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

    @Autowired
    private Oauth2Service oauth2Service;

    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        log.info("进入网关过滤器");
        String token = exchange.getRequest().getHeaders().getFirst("Authorization");
        String path = exchange.getRequest().getPath().toString();
        String httpMethod = exchange.getRequest().getMethodValue();
        Result result = oauth2Service.authentication(token, path, httpMethod);

        // 根据结果进行返回
        if (Result.SUCCESS.equals(result.getCode())) {
            return chain.filter(exchange);
        }

        exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);
        return exchange.getResponse().setComplete();

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
