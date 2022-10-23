package com.goudong.commons.exception.enumerate;

import com.goudong.commons.exception.BasicException;
import com.goudong.commons.exception.ExceptionEnumInterface;
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
     * {@code 500 Internal Server Error}.
     * <p>服务器遇到了不知道如何处理的情况。</p>
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.1">HTTP/1.1: Semantics and Content, section 6.6.1</a>
     */
    SERVER_ERROR(500, "500", "服务器内部错误，请联系管理员。", "500 Internal Server Error - 服务器遇到了不知道如何处理的情况。"),

    /**
     * {@code 501 Not Implemented}.<br>
     * 状态码表示服务器执行了该操作,不支持满足请求所需的功能
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.2">HTTP/1.1: Semantics and Content, section 6.6.2</a>
     */
    NOT_IMPLEMENTED(501, "Not Implemented"),

    /**
     * {@code 502 Bad Gateway}.
     * <p>此错误响应表明服务器作为网关需要得到一个处理这个请求的响应，但是得到一个错误的响应。</p>
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.3">HTTP/1.1: Semantics and Content, section 6.6.3</a>
     */
    BAD_GATEWAY(502, "502", "服务器内部错误，请联系管理员。", "502 Bad Gateway - 此错误响应表明服务器作为网关需要得到一个处理这个请求的响应，但是得到一个错误的响应。"),

    /**
     * {@code 503 Service Unavailable}.<br>
     * 服务器没有准备好处理请求。 常见原因是服务器因维护或重载而停机。 请注意，与此响应一起，应发送解释问题的用户友好页面。 这个响应应该用于临时条件和 Retry-After：如果可能的话，HTTP头应该包含恢复服务之前的估计时间。 网站管理员还必须注意与此响应一起发送的与缓存相关的标头，因为这些临时条件响应通常不应被缓存。
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.4">HTTP/1.1: Semantics and Content, section 6.6.4</a>
     */
    SERVICE_UNAVAILABLE(503, "503", "服务器没有准备好处理请求", "503 Service Unavailable -  服务器没有准备好处理请求。 常见原因是服务器因维护或重载而停机"),

    /**
     * {@code 504 Gateway Timeout}.
     * <p>504(网关超时)状态码表示服务器，当作为网关或代理时，没有收到及时的响应它需要从上游服务器访问以便完成请求。</p>
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.5">HTTP/1.1: Semantics and Content, section 6.6.5</a>
     */
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),
    /**
     * {@code 505 HTTP Version Not Supported}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.6.6">HTTP/1.1: Semantics and Content, section 6.6.6</a>
     */
    HTTP_VERSION_NOT_SUPPORTED(505, "HTTP Version not supported"),
    /**
     * {@code 506 Variant Also Negotiates}
     * @see <a href="https://tools.ietf.org/html/rfc2295#section-8.1">Transparent Content Negotiation</a>
     */
    VARIANT_ALSO_NEGOTIATES(506, "Variant Also Negotiates"),
    /**
     * {@code 507 Insufficient Storage}
     * @see <a href="https://tools.ietf.org/html/rfc4918#section-11.5">WebDAV</a>
     */
    INSUFFICIENT_STORAGE(507, "Insufficient Storage"),
    /**
     * {@code 508 Loop Detected}
     * @see <a href="https://tools.ietf.org/html/rfc5842#section-7.2">WebDAV Binding Extensions</a>
     */
    LOOP_DETECTED(508, "Loop Detected"),
    /**
     * {@code 509 Bandwidth Limit Exceeded}
     */
    BANDWIDTH_LIMIT_EXCEEDED(509, "Bandwidth Limit Exceeded"),
    /**
     * {@code 510 Not Extended}
     * @see <a href="https://tools.ietf.org/html/rfc2774#section-7">HTTP Extension Framework</a>
     */
    NOT_EXTENDED(510, "Not Extended"),
    /**
     * {@code 511 Network Authentication Required}.
     * @see <a href="https://tools.ietf.org/html/rfc6585#section-6">Additional HTTP Status Codes</a>
     */
    NETWORK_AUTHENTICATION_REQUIRED(511, "Network Authentication Required"),
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

    ServerExceptionEnum(int status, String clientMessage){
        this.status = status;
        this.code = String.valueOf(status);
        this.clientMessage = clientMessage;
        this.serverMessage = status + " " + clientMessage;
    }

    ServerExceptionEnum(int status, String code, String clientMessage, String serverMessage){
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }

    /**
     * 包含状态码
     *
     * @param status http状态码
     * @return true 包含；false 不包含
     */
    @Override
    public boolean containStatus(int status) {
        if (status >= 500 && status < 600) {
            return true;
        }
        return false;
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
