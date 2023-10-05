package com.goudong.authentication.common.util;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 *
 * @Author Administrator
 * @Version 1.0
 */
public class HttpRequestUtil {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 获取请求头中{@code X-App-Id}的值
     * @return 应用Id
     */
    public static Long getXAppId() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String appId = request.getHeader("X-App-Id");
        return Long.parseLong(appId);
    }
}
