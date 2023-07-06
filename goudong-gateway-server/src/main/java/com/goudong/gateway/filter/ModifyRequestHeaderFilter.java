package com.goudong.gateway.filter;

import com.alibaba.nacos.common.utils.CollectionUtils;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.gateway.util.HttpHeaderUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 处理简单的修改请求头信息
 * @author cfl
 * @date 2022/8/3 4:13
 * @version 1.0
 */
@Slf4j
@Component
public class ModifyRequestHeaderFilter implements GlobalFilter, Ordered {

    /**
     * 优先级要低于一般过滤器
     * @return
     */
    @Override
    public int getOrder() {
        return 10;
    }

    /**
     * 拦截器
     * @param exchange
     * @param chain
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();

        List<String> removeHeaders = new ArrayList<>();
        // 处理X-Inner请求头
        if (headers.getFirst(HttpHeaderConst.X_INNER) != null) {
            LogUtil.info(log, "本次请求携带了请求头{}={}，试图伪造内部接口请求。", HttpHeaderConst.X_INNER, headers.getFirst(HttpHeaderConst.X_INNER));
            removeHeaders.add(HttpHeaderConst.X_INNER);
        }

        // 存在需要删除的请求头，执行删除操作。
        if (CollectionUtils.isNotEmpty(removeHeaders)) {
            removeRequestHeaders(exchange, removeHeaders);
        }

        HttpHeaderUtil.getXRealIp(exchange);
        HttpHeaderUtil.getXTraceId(exchange);
        return chain.filter(exchange);
    }

    /**
     * 删除请求头信息
     * @param exchange
     * @param removeHeaders 被删除的请求头集合
     */
    private void removeRequestHeaders(ServerWebExchange exchange, List<String> removeHeaders) {
        ServerHttpRequest.Builder requestBuilder = exchange.getRequest().mutate();
        // 删除请求头
         requestBuilder.headers(k -> {
             removeHeaders.stream().forEach(rh -> {
                 k.remove(rh);
             });
         });
        ServerHttpRequest request = requestBuilder.build();
        exchange.mutate().request(request).build();
    }
}
