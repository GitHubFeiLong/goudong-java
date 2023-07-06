package com.goudong.gateway.filter.out;

import com.goudong.boot.web.util.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 类描述：
 * 移除链路追踪
 * @author cfl
 * @version 1.0
 * @date 2023/7/6 20:31
 */
@Slf4j
@Component("TraceIdOutFilter")
public class TraceIdFilter implements GlobalFilter, Ordered {


    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        TraceIdUtil.remove();
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
