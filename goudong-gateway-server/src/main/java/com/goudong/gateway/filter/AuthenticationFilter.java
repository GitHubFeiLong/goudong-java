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

        // 本次请求是登录认证时，不需要进行后面的处理
        if (uri.contains("/authentication/login")) {
            return chain.filter(exchange);
        }

        // 获取请求头Authorization对应的属性值
        List<String> tokenList = request.getHeaders().get(HttpHeaders.AUTHORIZATION);
        String token = null;
        if (!CollectionUtils.isEmpty(tokenList)) {
            token = tokenList.get(0);
        }

        // 获取cookie，用来做用户标识
        String cookie = request.getHeaders().getFirst(HttpHeaders.COOKIE);
        if (cookie != null && cookie.contains("=")) {
            cookie = cookie.substring(cookie.indexOf("=") + 1);
        }

        // 鉴权，返回用户信息
        // 注意：内部服务间使用Feign调用，不会走网关！所以也就不会在进行鉴权！
        // 如果需求是内部服务也要鉴权，那么就需要每个服务都添加全局拦截器，调用下面这个鉴权方法即可！！
        BaseUserDTO baseUserDTO = goudongOauth2ServerService.authorize(uri, method, token, cookie).getData();

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
