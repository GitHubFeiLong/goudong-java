package com.goudong.gateway.filter;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.web.core.ApiLog;
import com.goudong.boot.web.properties.ApiLogProperties;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * json
     */
    private final ObjectMapper objectMapper;

    private final ApiLogProperties apiLogProperties;

    //~construct methods
    //==================================================================================================================
    public AuthenticationFilter(GoudongOauth2ServerService goudongOauth2ServerService, ObjectMapper objectMapper, ApiLogProperties apiLogProperties) {
        this.goudongOauth2ServerService = goudongOauth2ServerService;
        this.objectMapper = objectMapper;
        this.apiLogProperties = apiLogProperties;
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
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();

        String uri = request.getURI().getPath();
        String method = request.getMethodValue();
        // 计时器，当是开启接口打印时才创建对象，提高程序性能
        StopWatch stopWatch = null;
        ApiLog apiLog = null;
        if (apiLogProperties.getEnabled()) {
            stopWatch = new StopWatch();  // 创建计时器
            stopWatch.start();
            // String ip = IpUtil.getStringIp(request);
            String ip = "";

            Map<String, String> requestHead = getRequestHead(request);
            Object params = getArgs(request);

            // 创建apiLog对象
            apiLog = new ApiLog();
            apiLog.setIp(ip);
            apiLog.setUri(uri);
            apiLog.setMethod(method);
            apiLog.setHeadParams(requestHead);
            apiLog.setParams(params);
        }
        boolean successful = true;
        try {
            // 本次请求是登录认证/刷新令牌时，不需要进行后面的token校验
            if (uri.contains("/authentication/login") || uri.contains("/api/oauth2/authentication/refresh-token")) {
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
        } catch (Exception e) {
            successful = false;
            throw e;
        } finally {
            if (apiLogProperties.getEnabled()) {
                stopWatch.stop();
                long time = stopWatch != null ? stopWatch.getTotalTimeMillis() : -1;
                apiLog.setResults("");
                apiLog.setSuccessful(successful);
                apiLog.setTime(time);

                // 输出接口日志
                apiLog.printLogString(apiLogProperties, objectMapper);
            }
        }

    }

    /**
     * 获取本次请求头
     * @param request
     * @return
     */
    private Map<String, String> getRequestHead(ServerHttpRequest request) {
        request.getHeaders().entrySet().forEach(p->{
            log.debug("{} -> {}", p.getKey(), p.getValue());
        });
        return new HashMap<>();
    }


    /**
     * 获取请求参数
     * @param joinPoint
     * @return
     */
    private Object getArgs(ServerHttpRequest request) {
        // List<Class> filter = ListUtil.newArrayList(
        //
        // );
        if ("GET".equals(request.getMethodValue())) {
            return request.getQueryParams();
        }

        return request.getBody();

    }
}
