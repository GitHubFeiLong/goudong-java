package com.goudong.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.web.properties.ApiLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 类描述：
 * 请求拦截器
 * @author cfl
 * @version 1.0
 * @date 2023/4/18 9:25
 */
@Slf4j
@Component
public class HttpRequestFilter implements GlobalFilter, Ordered {

    @Resource
    private ApiLogProperties apiLogProperties;

    @Resource
    private ObjectMapper objectMapper;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpMethod method = exchange.getRequest().getMethod();
        switch (method) {
            case GET:
                return handlerGet(exchange, chain);
            case POST:
                break;
            case PUT:
                break;
            case DELETE:
                break;
            default:
                return chain.filter(exchange);
        }

        return chain.filter(exchange);
    }

    /**
     * 处理Get请求
     * @param exchange
     * @param chain
     * @return
     */
    private Mono<Void> handlerGet(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        //  开启了日志
        if (apiLogProperties.getEnabled()) {

        }
        if ("application/x-www-form-urlencoded".equals(contentType)) {
            MultiValueMap<String, String> queryParams = request.getQueryParams();
            log.info("请求参数：{}", queryParams);
        }

        if ("multipart/form-data".equals(contentType)) {

        }
        if ("application/json".equals(contentType)) {

        }

        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
