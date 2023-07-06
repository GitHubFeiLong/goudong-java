package com.goudong.commons.utils.core;

import com.goudong.core.util.StringUtil;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 获取请求对象的IP地址
 * @ClassName IpUtil
 * @Author msi
 * @Date 2020/6/11 19:33
 * @Version 1.0
 */
public class IpUtil {
    public static final String UNKNOWN = "unknown";

    /**
     * 获取请求的IP地址以long类型返回
     * @param request
     * @return
     */
    public static long getLongIp(HttpServletRequest request) {
        String ipString = IpUtil.getStringIp(request);

        return IpUtil.ipToLong(ipString);
    }

    /**
     * 获取请求的IP地址以long类型返回
     * @return
     */
    public static long getLongIp(String ipString) {
        return IpUtil.ipToLong(ipString);
    }

    /**
     * 获取Ip地址
     * @param request 请求对象
     * @return ip地址
     */
    public static String getStringIp(HttpServletRequest request) {
        String xIp = request.getHeader("X-Real-IP");
        if (StringUtil.isNotBlank(xIp)) {
            return xIp;
        }
        String xFor = request.getHeader("X-Forwarded-For");
        if(StringUtils.isNotEmpty(xFor) && !IpUtil.UNKNOWN.equalsIgnoreCase(xFor)){
            //多次反向代理后会有多个ip值，第一个ip才是真实ip
            int index = xFor.indexOf(",");
            if(index != -1){
                return xFor.substring(0,index);
            }else{
                return xFor;
            }
        }

        return "localhost";
    }

    /**
     * 获取ip地址
     * @param request
     * @param xIp
     * @return
     */
    private static String getIp(HttpServletRequest request, String xIp) {
        String xFor;
        xFor = xIp;
        if(StringUtils.isNotEmpty(xFor) && !IpUtil.UNKNOWN.equalsIgnoreCase(xFor)){
            return xFor;
        }
        if (StringUtils.isBlank(xFor) || IpUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || IpUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(xFor) || IpUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_CLIENT_IP");
        }
        if (StringUtils.isBlank(xFor) || IpUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (StringUtils.isBlank(xFor) || IpUtil.UNKNOWN.equalsIgnoreCase(xFor)) {
            xFor = request.getRemoteAddr();
        }
        return xFor;
    }

    /**
     * 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
     * 对应MySQL的INET_ATON函数
     * @param strIp
     * @return
     */
    public static long ipToLong(String strIp){
        long[] ip = new long[4];

        if (strIp.contains(".")) {
            //先找到IP地址字符串中.的位置
            int position1 = strIp.indexOf(".");
            int position2 = strIp.indexOf(".", position1 + 1);
            int position3 = strIp.indexOf(".", position2 + 1);
            //将每个.之间的字符串转换成整型
            ip[0] = Long.parseLong(strIp.substring(0, position1));
            ip[1] = Long.parseLong(strIp.substring(position1+1, position2));
            ip[2] = Long.parseLong(strIp.substring(position2+1, position3));
            ip[3] = Long.parseLong(strIp.substring(position3+1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        }

        return 0;
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     * 对应MySQL的INET_NTOA函数
     * @param longIp
     * @return
     */
    public static String longToIP(long longIp){
        StringBuffer sb = new StringBuffer("");
        //直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    private IpUtil(){}
}

