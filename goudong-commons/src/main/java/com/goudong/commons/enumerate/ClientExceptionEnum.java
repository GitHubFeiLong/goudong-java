package com.goudong.commons.enumerate;

import lombok.Getter;

/**
 * 类描述：
 *  客户端错误
 *  400~417
 * @See https://www.restapitutorial.com/httpstatuscodes.html
 * @Author msi
 * @Date 2020/10/17 16:29
 * @Version 1.0
 */
@Getter
public enum ClientExceptionEnum {

    NOT_AUTHENTICATION(401, "401000", "请登录", "用户未登录"),
    AUTHENTICATION_EXPIRES(401, "401001", "登录过期", "登录过期"),
    NOT_AUTHORIZATION(403, "403001", "无权访问", "用户没有权限"),

    ;
    /**
     * 响应码
     */
    private int status;
    /**
     * 错误代码
     */
    private String code;

    /**
     * 客户看见的提示信息
     */
    private String clientMessage;
    /**
     * 服务器日志信息
     */
    private String serverMessage;

    ClientExceptionEnum(int status, String code, String clientMessage, String serverMessage){
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }
}
