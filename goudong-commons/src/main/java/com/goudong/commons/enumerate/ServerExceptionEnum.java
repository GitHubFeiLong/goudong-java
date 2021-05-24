package com.goudong.commons.enumerate;

import lombok.Getter;

/**
 * 类描述：
 *  服务端错误
 *
 * @See https://www.restapitutorial.com/httpstatuscodes.html
 * @Author msi
 * @Date 2020/10/17 16:29
 * @Version 1.0
 */
@Getter
public enum ServerExceptionEnum implements ExceptionEnumInterface {
    SERVER_ERROR(500, "500", "服务器内部错误，请联系网管", "未捕获的未知异常"),
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

    ServerExceptionEnum(int status, String code, String clientMessage, String serverMessage){
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }
}
