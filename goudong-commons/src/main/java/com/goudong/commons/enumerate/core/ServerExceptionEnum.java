package com.goudong.commons.enumerate.core;

import com.goudong.commons.exception.BasicException;
import com.goudong.commons.exception.ServerException;
import lombok.Getter;

/**
 * 类描述：
 *  服务端错误
 * 500~511
 * @See https://www.restapitutorial.com/httpstatuscodes.html
 * @Author msi
 * @Date 2020/10/17 16:29
 * @Version 1.0
 */
@Getter
public enum ServerExceptionEnum implements ExceptionEnumInterface {
    /**
     * 500 Internal Server Error
     * 服务器遇到了不知道如何处理的情况。
     */
    SERVER_ERROR(500, "500", "服务器内部错误，请联系管理员。", "500 Internal Server Error - 服务器遇到了不知道如何处理的情况。"),

    /**
     * 502 Bad Gateway
     * 此错误响应表明服务器作为网关需要得到一个处理这个请求的响应，但是得到一个错误的响应。
     */
    BAD_GATEWAY(502, "502", "服务器内部错误，请联系管理员。", "502 Bad Gateway - 此错误响应表明服务器作为网关需要得到一个处理这个请求的响应，但是得到一个错误的响应。"),

    /**
     * 503 Service Unavailable
     * 服务器没有准备好处理请求。 常见原因是服务器因维护或重载而停机。 请注意，与此响应一起，应发送解释问题的用户友好页面。 这个响应应该用于临时条件和 Retry-After：如果可能的话，HTTP头应该包含恢复服务之前的估计时间。 网站管理员还必须注意与此响应一起发送的与缓存相关的标头，因为这些临时条件响应通常不应被缓存。
     */
    SERVICE_UNAVAILABLE(503, "503", "服务器没有准备好处理请求", "503 Service Unavailable -  服务器没有准备好处理请求。 常见原因是服务器因维护或重载而停机"),




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


    @Override
    public BasicException server() {
        return ServerException.server(this);
    }

    @Override
    public BasicException server(String serverMessage) {
        return ServerException.server(this, serverMessage);
    }

    @Override
    public BasicException server(String clientMessage, String serverMessage) {
        return ServerException.server(this, clientMessage, serverMessage);
    }

    @Override
    public BasicException server(String serverMessageTemplate, Object[] serverMessageParams) {
        return ServerException.server(this, serverMessageTemplate, serverMessageParams);
    }

    @Override
    public BasicException server(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ServerException.server(this, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    @Override
    public BasicException server(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ServerException.server(this, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }
}
