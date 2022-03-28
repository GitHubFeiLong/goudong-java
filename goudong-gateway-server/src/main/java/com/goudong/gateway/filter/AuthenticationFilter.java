package com.goudong.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.commons.utils.core.LogUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.util.List;

/**
 * 类描述：
 * 网关统一鉴权拦截器
 * @Author e-Feilong.Chen
 * @Date 2022/2/18 11:19
 */
@Slf4j
@Component
public class AuthenticationFilter implements GlobalFilter, Ordered {
    //~fields
    //==================================================================================================================
    /**
     * oauth2服务
     */
    private final GoudongOauth2ServerService goudongOauth2ServerService;

    //~construct methods
    //==================================================================================================================
    public AuthenticationFilter(GoudongOauth2ServerService goudongOauth2ServerService) {
        this.goudongOauth2ServerService = goudongOauth2ServerService;
    }

    //~methods
    //==================================================================================================================

    /**
     * 优先级，优先级应该比其它所有过滤器都要高
     * @return
     */
    @Override
    public int getOrder() {
        return -100;
    }

    /**
     *
     * @param exchange ServerWebExchange是一个HTTP请求-响应交互的契约。提供对HTTP请求和响应的访问，
     *                 并公开额外的 服务器 端处理相关属性和特性，如请求属性。
     * @param chain
     * @return
     */
    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        LogUtil.debug(log, "进入网关鉴权拦截器");
        ServerHttpRequest request = exchange.getRequest();
        String uri = request.getURI().getPath();
        String method = request.getMethodValue();
        // 打印详细日志
        if (log.isDebugEnabled()) {
            log.debug("");
            log.debug("【{}】 {}", request.getMethodValue(), uri);
            request.getHeaders().entrySet().forEach(p->{
                log.debug("{} -> {}", p.getKey(), p.getValue());
            });
        }

        List<String> tokenList = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        String token = null;
        if (!CollectionUtils.isEmpty(tokenList)) {
            token = tokenList.get(0);
        }
        // 鉴权，返回用户信息
        BaseUserDTO baseUserDTO = goudongOauth2ServerService.authorize(uri, method, token).getData();

        // 将用户信息保存到请求头中，供下游服务使用
        ServerHttpRequest newRequest = request
                .mutate()
                .header(HttpHeaderConst.X_REQUEST_USER, URLEncoder.encode(JSON.toJSONString(baseUserDTO), "UTF-8"))
                .build();

        // 创建新的交换机
        ServerWebExchange newExchange = exchange.mutate().request(newRequest).build();

        return chain.filter(newExchange);
    }



}
