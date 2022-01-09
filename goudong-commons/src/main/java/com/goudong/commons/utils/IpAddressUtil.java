package com.goudong.commons.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 获取请求对象的IP地址
 * @ClassName IpAddressUtil
 * @Author msi
 * @Date 2020/6/11 19:33
 * @Version 1.0
 */
@Deprecated
public class IpAddressUtil {
    public static final String UNKNOWN = "unknown";
    /**
     * 获取Ip地址
     * @param request 请求对象
     * @return ip地址
     */
    public static String getIpAddress(HttpServletRequest request) {
        String xIp = request.getHeader("X-Real-IP");
        String xFor = request.getHeader("X-Forwarded-For");

        if(StringUtils.isNotEmpty(xFor) && !IpAddressUtil.UNKNOWN.equalsIgnoreCase(xFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xFor.indexOf(",");
            if(index != -1){
                return xFor.substring(0,index);
            }else{
                return xFor;
            }
        }

        return getIp(request, xIp);
    }

    private static String getIp(HttpServletRequest request, String xIp) {
        String xFor;
        xFor = xIp;
        if(StringUtils.isNotEmpty(xFor) && !IpAddressUtil.UNKNOWN.equalsIgnoreCase(xFor)){
            return xFor;
        }
        if (StringUtils.isBlank(xFor) || IpAddressUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || IpAddressUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || IpAddressUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xFor) || IpAddressUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xFor) || IpAddressUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }
        return xFor;
    }

    private IpAddressUtil(){}
}

