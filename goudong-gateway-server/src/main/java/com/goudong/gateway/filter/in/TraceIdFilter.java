package com.goudong.gateway.filter.in;

import com.goudong.boot.web.util.TraceIdUtil;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.core.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 类描述：
 * 添加链路追踪
 * @author cfl
 * @version 1.0
 * @date 2023/7/6 20:31
 */
@Slf4j
@Component("TraceIdInFilter")
public class TraceIdFilter implements GlobalFilter, Ordered {


    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String traceId = exchange.getRequest().getHeaders().getFirst(HttpHeaderConst.X_TRACE_ID);
        if (StringUtil.isNotBlank(traceId)) {
            TraceIdUtil.put(traceId);
        } else {
            traceId = TraceIdUtil.put();
        }
        log.debug("traceId:{}", traceId);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}
