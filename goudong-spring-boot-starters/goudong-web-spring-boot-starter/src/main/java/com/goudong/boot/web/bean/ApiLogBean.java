package com.goudong.boot.web.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.web.core.ApiLog;
import com.goudong.boot.web.properties.ApiLogProperties;
import com.goudong.boot.web.util.IpUtil;
import com.goudong.boot.web.util.TraceIdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 *
 * @author chenf
 */
@Slf4j
public class ApiLogBean {
    //~fields
    //==================================================================================================================
    private final ObjectMapper objectMapper;

    private final ApiLogProperties apiLogProperties;

    public ApiLogBean(ObjectMapper objectMapper, ApiLogProperties apiLogProperties) {
        if (log.isDebugEnabled()) {
            log.debug("注入apiLogAop");
        }
        this.objectMapper = objectMapper;
        this.apiLogProperties = apiLogProperties;
    }
    //~methods
    //==================================================================================================================

    /**
     * 打印请求日志
     * @param result    响应结果
     */
    public void print(Object result) {
        // 开启接口打印时
        if (apiLogProperties.getEnabled()) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ip = IpUtil.getStringIp(request);
            String requestUri = request.getRequestURI();
            String method = request.getMethod();
            Map<String, String> requestHead = getRequestHead(request);

            // 创建apiLog对象
            ApiLog apiLog = new ApiLog();
            apiLog.setIp(ip);
            apiLog.setUri(requestUri);
            apiLog.setMethod(method);
            apiLog.setHeadParams(requestHead);
            apiLog.setResults(result);
            apiLog.setTranceId(TraceIdUtil.get());
            // 输出接口日志
            apiLog.printLogString(apiLogProperties, objectMapper);
        }
    }

    /**
     * 获取x-开头的head参数和Authorization参数
     * @param request
     * @return
     */
    private Map<String, String> getRequestHead(HttpServletRequest request){
        //获取请求参数
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> data = new HashMap<>();
        ApiLogProperties.PrintLogLimit printLogLimit = apiLogProperties.getPrintLogLimit();
        // 自定义配置的请求头限制
        Map<String, Boolean> headParams = printLogLimit.getHeadParams();
        while (headerNames.hasMoreElements()) {
            // 请求头转小写
            String name = headerNames.nextElement().toLowerCase();
            if (headParams.containsKey(name) && headParams.get(name)) {
                data.put(name, request.getHeader(name));
                continue;
            }
            if (name.contains("x-") && !headParams.containsKey(name)) {
                data.put(name, request.getHeader(name));
                continue;
            }

        }
        return data;
    }
}
