package com.goudong.boot.web.enumerate;

import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.core.ClientException;
import org.springframework.http.HttpStatus;

/**
 * 类描述：
 *  客户端错误
 *  400~417
 * @See https://www.restapitutorial.com/httpstatuscodes.html
 * @see HttpStatus
 * @Author msi
 * @Date 2020/10/17 16:29
 * @Version 1.0
 */
public enum ClientExceptionEnum implements ExceptionEnumInterfaceExt {
    /**
     * {@code 400 Bad Request}.
     * <p>1、语义有误，当前请求无法被服务器理解。除非进行修改，否则客户端不应该重复提交这个请求。</p>
     * <p>2、请求参数有误。</p>
     *
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.1">HTTP/1.1: Semantics and Content, section 6.5.1</a>
     */
    BAD_REQUEST(400, "400", "参数错误", "400 Bad Request - 语义有误，当前请求无法被服务器理解。除非进行修改，否则客户端不应该重复提交这个请求;请求参数有误"),

    /**
     * {@code 401 Unauthorized}.<br>
     * 当前请求需要用户验证。该响应必须包含一个适用于被请求资源的 WWW-Authenticate 信息头用以询问用户信息。客户端可以重复提交一个包含恰当的 Authorization 头信息的请求。如果当前请求已经包含了 Authorization 证书，那么401响应代表着服务器验证已经拒绝了那些证书。如果401响应包含了与前一个响应相同的身份验证询问，且浏览器已经至少尝试了一次验证，那么浏览器应当向用户展示响应中包含的实体信息，因为这个实体信息中可能包含了相关诊断信息。
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-3.1">HTTP/1.1: Authentication, section 3.1</a>
     */
    UNAUTHORIZED(401, "401", "请登录认证", "401 Unauthorized - 用户未认证，或认证信息过期"),

    /**
     * {@code 402 Payment Required}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.2">HTTP/1.1: Semantics and Content, section 6.5.2</a>
     */
    PAYMENT_REQUIRED(402, "402", "Payment Required", "402 Payment Required"),

    /**
     * {@code 403 Forbidden}.
     * <p>服务器已经理解请求，但是拒绝执行它。</p>
     * <p>与 401 响应不同的是，身份验证并不能提供任何帮助，而且这个请求也不应该被重复提交。</p>
     * <p>如果这不是一个 HEAD 请求，而且服务器希望能够讲清楚为何请求不能被执行，那么就应该在实体内描述拒绝的原因。</p>
     * <p>当然服务器也可以返回一个 404 响应，假如它不希望让客户端获得任何信息。</p>
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.3">HTTP/1.1: Semantics and Content, section 6.5.3</a>
     */
    FORBIDDEN(403, "403", "无权访问", "403 Forbidden - 没有权限"),

    /**
     * {@code 404 Not Found}.
     * <p>请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。</p>
     * <p>假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。</p>
     * <p>404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。</p>
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.4">HTTP/1.1: Semantics and Content, section 6.5.4</a>
     */
    NOT_FOUND(404, "404", "资源不存在", "404 Not Found - 请求失败，请求所希望得到的资源未被在服务器上发现。"),

    /**
     * {@code 405 Method Not Allowed}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.5">HTTP/1.1: Semantics and Content, section 6.5.5</a>
     */
    METHOD_NOT_ALLOWED(405, "405", "不支持方法", "506 Method Not Allowed - 请求失败，不允许使用该方法"),

    /**
     * {@code 406 Not Acceptable}.
     * <p>请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。</p>
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.6">HTTP/1.1: Semantics and Content, section 6.5.6</a>
     */
    NOT_ACCEPTABLE(406, "406", "请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。", "406 Not Acceptable - 请求头不满足接口的条件"),

    /**
     * {@code 407 Proxy Authentication Required}.
     * @see <a href="https://tools.ietf.org/html/rfc7235#section-3.2">HTTP/1.1: Authentication, section 3.2</a>
     */
    PROXY_AUTHENTICATION_REQUIRED(407, "407","Proxy Authentication Required", "Proxy Authentication Required"),

    /**
     * {@code 408 Request Timeout}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.7">HTTP/1.1: Semantics and Content, section 6.5.7</a>
     */
    REQUEST_TIMEOUT(408, "408", "请求超时","408 Request Timeout - 请求超时"),

    /**
     * {@code 409 Conflict}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.8">HTTP/1.1: Semantics and Content, section 6.5.8</a>
     */
    CONFLICT(409,"Conflict"),

    /**
     * {@code 410 Gone}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.9">
     *     HTTP/1.1: Semantics and Content, section 6.5.9</a>
     */
    GONE(410, "Gone"),

    /**
     * {@code 411 Length Required}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.10">
     *     HTTP/1.1: Semantics and Content, section 6.5.10</a>
     */
    LENGTH_REQUIRED(411, "Length Required"),
    /**
     * {@code 412 Precondition failed}.
     * @see <a href="https://tools.ietf.org/html/rfc7232#section-4.2">
     *     HTTP/1.1: Conditional Requests, section 4.2</a>
     */
    PRECONDITION_FAILED(412, "Precondition Failed"),
    /**
     * {@code 413 Payload Too Large}.
     * @since 4.1
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.11">
     *     HTTP/1.1: Semantics and Content, section 6.5.11</a>
     */
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),
    /**
     * {@code 414 URI Too Long}.
     * @since 4.1
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.12">
     *     HTTP/1.1: Semantics and Content, section 6.5.12</a>
     */
    URI_TOO_LONG(414, "URI Too Long"),
    /**
     * {@code 415 Unsupported Media Type}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.13">
     *     HTTP/1.1: Semantics and Content, section 6.5.13</a>
     */
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),
    /**
     * {@code 416 Requested Range Not Satisfiable}.
     * @see <a href="https://tools.ietf.org/html/rfc7233#section-4.4">HTTP/1.1: Range Requests, section 4.4</a>
     */
    REQUESTED_RANGE_NOT_SATISFIABLE(416, "Requested range not satisfiable"),
    /**
     * {@code 417 Expectation Failed}.
     * @see <a href="https://tools.ietf.org/html/rfc7231#section-6.5.14">
     *     HTTP/1.1: Semantics and Content, section 6.5.14</a>
     */
    EXPECTATION_FAILED(417, "Expectation Failed"),
    /**
     * {@code 418 I'm a teapot}.
     * @see <a href="https://tools.ietf.org/html/rfc2324#section-2.3.2">HTCPCP/1.0</a>
     */
    I_AM_A_TEAPOT(418, "I'm a teapot"),
    /**
     * 422 Unprocessable Entity
     * 请求格式良好，但由于语义错误而无法遵循。
     */
    UNPROCESSABLE_ENTITY(422, "422", "请求格式良好，但由于语义错误而无法遵循。", "422 Unprocessable Entity - 请求格式良好，但由于语义错误而无法遵循。"),
    /**
     * {@code 423 Locked}.
     * @see <a href="https://tools.ietf.org/html/rfc4918#section-11.3">WebDAV</a>
     */
    LOCKED(423, "Locked"),
    /**
     * {@code 424 Failed Dependency}.
     * @see <a href="https://tools.ietf.org/html/rfc4918#section-11.4">WebDAV</a>
     */
    FAILED_DEPENDENCY(424, "Failed Dependency"),
    /**
     * {@code 425 Too Early}.
     * @since 5.2
     * @see <a href="https://tools.ietf.org/html/rfc8470">RFC 8470</a>
     */
    TOO_EARLY(425, "Too Early"),
    /**
     * {@code 426 Upgrade Required}.
     * @see <a href="https://tools.ietf.org/html/rfc2817#section-6">Upgrading to TLS Within HTTP/1.1</a>
     */
    UPGRADE_REQUIRED(426, "Upgrade Required"),
    /**
     * {@code 428 Precondition Required}.
     * @see <a href="https://tools.ietf.org/html/rfc6585#section-3">Additional HTTP Status Codes</a>
     */
    PRECONDITION_REQUIRED(428, "Precondition Required"),
    /**
     * {@code 429 Too Many Requests}.
     * 用户在给定的时间内发送了太多请求（“限制请求速率”）。
     * @see <a href="https://tools.ietf.org/html/rfc6585#section-4">Additional HTTP Status Codes</a>
     */
    TOO_MANY_REQUESTS(429, "429", "服务器繁忙，请稍后重试", "429 Too Many Requests - 该资源限制用户重复提交请求"),

    /**
     * {@code 431 Request Header Fields Too Large}.
     * @see <a href="https://tools.ietf.org/html/rfc6585#section-5">Additional HTTP Status Codes</a>
     */
    REQUEST_HEADER_FIELDS_TOO_LARGE(431, "Request Header Fields Too Large"),
    /**
     * {@code 451 Unavailable For Legal Reasons}.
     * @see <a href="https://tools.ietf.org/html/draft-ietf-httpbis-legally-restricted-status-04">
     * An HTTP Status Code to Report Legal Obstacles</a>
     * @since 4.3
     */
    UNAVAILABLE_FOR_LEGAL_REASONS(451, "Unavailable For Legal Reasons"),
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

    ClientExceptionEnum(int status, String clientMessage){
        this.status = status;
        this.code = String.valueOf(status);
        this.clientMessage = clientMessage;
        this.serverMessage = status + " " + clientMessage;
    }

    ClientExceptionEnum(int status, String code, String clientMessage, String serverMessage){
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
        if (status >= 400 && status < 500) {
            return true;
        }
        return false;
    }

    @Override
    public BasicException client() {
        return ClientException.client(this);
    }

    @Override
    public BasicException client(String clientMessage) {
        return ClientException.client(this, clientMessage);
    }

    @Override
    public BasicException client(String clientMessage, String serverMessage) {
        return ClientException.client(this, clientMessage, serverMessage);
    }

    @Override
    public BasicException client(String clientMessageTemplate, Object[] clientMessageParams) {
        return ClientException.client(this, clientMessageTemplate, clientMessageParams);
    }

    @Override
    public BasicException client(String clientMessageTemplate, Object[] clientMessageParams, String serverMessage) {
        return ClientException.client(this, clientMessageTemplate, clientMessageParams, serverMessage);
    }

    @Override
    public BasicException client(String clientMessage, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(this, clientMessage, serverMessageTemplate, serverMessageParams);
    }

    @Override
    public BasicException client(String clientMessageTemplate, Object[] clientMessageParams, String serverMessageTemplate, Object[] serverMessageParams) {
        return ClientException.client(this, clientMessageTemplate, clientMessageParams, serverMessageTemplate, serverMessageParams);
    }
    //~getter
    //==================================================================================================================
    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getClientMessage() {
        return clientMessage;
    }

    @Override
    public String getServerMessage() {
        return serverMessage;
    }
}
