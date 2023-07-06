package com.goudong.gateway.util;

import com.goudong.boot.web.util.TraceIdUtil;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.StringUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

/**
 * 类描述：
 * 请求头工具类
 * @author cfl
 * @version 1.0
 * @date 2023/7/6 19:38
 */
public class HttpHeaderUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 获取全局链路追踪id
     * @param exchange
     * @return
     */
    public static String getXTraceId(ServerWebExchange exchange) {
        String traceId = exchange.getRequest().getHeaders().getFirst(HttpHeaderConst.X_TRACE_ID);
        if (StringUtil.isNotBlank(traceId)) {
            return traceId;
        }
        // 获取全局链路追踪id
        String finalTraceId = TraceIdUtil.get();
        AssertUtil.isNotBlank(finalTraceId);

        // 将其设置到请求头中
        exchange.getRequest().mutate().headers(httpHeaders -> httpHeaders.add(HttpHeaderConst.X_TRACE_ID, finalTraceId));

        return finalTraceId;
    }

    /**
     * 获取真实ip
     * @param exchange
     * @return
     */
    public static String getXRealIp(ServerWebExchange exchange) {
        String realIp = exchange.getRequest().getHeaders().getFirst(HttpHeaderConst.X_REAL_IP);
        if (StringUtil.isBlank(realIp)) {
            String ipAddress = exchange.getRequest().getHeaders().getFirst("X-Forwarded-For");
            if (ipAddress == null || ipAddress.isEmpty()) {
                ipAddress = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            }
            // 在这里可以使用获取到的真实IP地址进行相关处理
            System.out.println("客户端真实IP地址：" + ipAddress);

            String finalIpAddress = ipAddress;
            exchange.getRequest().mutate().headers(httpHeaders -> httpHeaders.add(HttpHeaderConst.X_REAL_IP, finalIpAddress));
            return ipAddress;
        }

        return realIp;
    }

    /**
     * 获取token
     * @param exchange
     * @return
     */
    public static String getAuthorization(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    }

    /**
     * 获取cookie
     * @param exchange
     * @return
     */
    public static String getCookie(ServerWebExchange exchange) {
        return exchange.getRequest().getHeaders().getFirst(HttpHeaders.COOKIE);
    }
}
