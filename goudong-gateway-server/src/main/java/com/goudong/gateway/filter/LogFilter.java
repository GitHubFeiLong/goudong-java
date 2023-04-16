package com.goudong.gateway.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.web.core.ApiLog;
import com.goudong.boot.web.properties.ApiLogProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 * 日志
 * @author cfl
 * @version 1.0
 * @date 2023/4/16 17:01
 */
@Slf4j
@Component
public class LogFilter implements GlobalFilter, Ordered {

    //~fields
    //==================================================================================================================
    @Resource
    private ApiLogProperties apiLogProperties;

    @Resource
    private ObjectMapper objectMapper;

    //~methods
    //==================================================================================================================
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        // 计时器，当是开启接口打印时才创建对象，提高程序性能
        StopWatch stopWatch = null;
        ApiLog apiLog = null;
        if (apiLogProperties.getEnabled()) {
            stopWatch = new StopWatch();  // 创建计时器
            stopWatch.start();
            // String ip = IpUtil.getStringIp(request);
            String ip = "";
            String uri = request.getURI().getPath();
            String method = request.getMethodValue();
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
        boolean successful = false;
        try {
            Mono<Void> filter = chain.filter(exchange);
            successful = true;
            return filter;
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


    @Override
    public int getOrder() {
        return HIGHEST_PRECEDENCE;
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
     * @param request
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
