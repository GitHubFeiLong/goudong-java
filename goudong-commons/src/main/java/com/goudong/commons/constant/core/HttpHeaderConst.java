package com.goudong.commons.constant.core;

/**
 * 类描述：
 * 存放自定义的Http Header的常量信息命名方式参看{@link org.springframework.http.HttpHeaders}，首字母大写
 * 自定义的头命名使用`X-`前缀，与规范的区分。
 * @see org.springframework.http.HttpHeaders
 * @author msi
 * @version 1.0
 * @date 2022/1/20 20:12
 */
public class HttpHeaderConst {

    //~fields
    //==================================================================================================================
    /**
     * 自定义请求头，标明指定终端类型
     * 令牌分发时和用户登录时都根据这个值进行分设备。
     */
    public static final String X_CLIENT_SIDE = "X-Client-Side";

    /**
     * 当鉴权过后，将当前用户转成json设置到请求头里面，下游服务通过拦截器拦截获取当前发起请求的用户信息
     */
    public static final String X_REQUEST_USER = "Request-User";

    /**
     * 前端使用AES加密请求参数的密钥信息
     */
    public static final String X_AES_KEY = "X-Aes-Key";

    //~methods
    //==================================================================================================================

}