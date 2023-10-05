package com.goudong.authentication.server.constant;

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

    //~自定义请求头
    //==================================================================================================================
    /**
     * 自定义请求头，标明指定终端类型
     * 令牌分发时和用户登录时都根据这个值进行分设备。
     */
    public static final String X_CLIENT_SIDE = "X-Client-Side";

    /**
     * 当鉴权过后，将当前用户转成json设置到请求头里面，下游服务通过拦截器拦截获取当前发起请求的用户信息
     */
    public static final String X_REQUEST_USER = "X-Request-User";

    /**
     * 前端使用AES加密请求参数的密钥信息
     */
    public static final String X_AES_KEY = "X-Aes-Key";

    /**
     * 内部服务之间进行调用，新增一个固定请求头，表明这是一个内部请求调用。
     * 而网关会在拦截器中，将该请求头去掉，防止伪造
     */
    public static final String X_INNER = "X-Inner";

    /**
     * 日志打印接口返回值的长度限制
     */
    public static final String X_API_RESULT_LENGTH = "X-Api-Result-Length";

    /**
     * 应用id
     */
    public static final String X_APP_ID = "X-App-Id";

    /**
     * 真实ip地址
     */
    public static final String X_REAL_IP = "X-Real-IP";

    /**
     * 全局链路id
     */
    public static final String X_TRACE_ID = "X-Trace-Id";

    //~methods
    //==================================================================================================================

}
