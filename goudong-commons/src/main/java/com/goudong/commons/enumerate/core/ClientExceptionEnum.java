package com.goudong.commons.enumerate.core;

import com.goudong.commons.enumerate.core.ExceptionEnumInterface;
import lombok.Getter;
import org.springframework.http.HttpStatus;

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
public enum ClientExceptionEnum implements ExceptionEnumInterface {
    /**
     * 400 Bad Request
     * 1、语义有误，当前请求无法被服务器理解。除非进行修改，否则客户端不应该重复提交这个请求。
     * 2、请求参数有误。
     */
    BAD_REQUEST(400, "400", "参数错误", "400 Bad Request - 语义有误，当前请求无法被服务器理解。除非进行修改，否则客户端不应该重复提交这个请求;请求参数有误"),

    /**
     * 401 Unauthorized
     * 当前请求需要用户验证。该响应必须包含一个适用于被请求资源的 WWW-Authenticate 信息头用以询问用户信息。客户端可以重复提交一个包含恰当的 Authorization 头信息的请求。如果当前请求已经包含了 Authorization 证书，那么401响应代表着服务器验证已经拒绝了那些证书。如果401响应包含了与前一个响应相同的身份验证询问，且浏览器已经至少尝试了一次验证，那么浏览器应当向用户展示响应中包含的实体信息，因为这个实体信息中可能包含了相关诊断信息。
     */
    UNAUTHORIZED(401, "401", "请登录认证", "401 Unauthorized - 用户未认证，或认证信息过期"),

    /**
     * 403 Forbidden
     * 服务器已经理解请求，但是拒绝执行它。与 401 响应不同的是，身份验证并不能提供任何帮助，而且这个请求也不应该被重复提交。如果这不是一个 HEAD 请求，而且服务器希望能够讲清楚为何请求不能被执行，那么就应该在实体内描述拒绝的原因。当然服务器也可以返回一个 404 响应，假如它不希望让客户端获得任何信息。
     */
    NOT_AUTHORIZATION(403, "403", "无权访问", "403 Forbidden - 没有权限"),

    /**
     * 404 Not Found
     * 请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。
     * 假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。
     * 404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。
     */
    NOT_FOUND(404, "404", "资源不存在", "404 Not Found - 请求失败，请求所希望得到的资源未被在服务器上发现。"),

    /**
     * 406 Not Acceptable
     * 请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。
     */
    NOT_ACCEPTABLE(406, "406", "请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。", "406 Not Acceptable - 请求头不满足接口的条件"),

    /**
     * 422 Unprocessable Entity
     * 请求格式良好，但由于语义错误而无法遵循。
     */
    UNPROCESSABLE_ENTITY(422, "422", "请求格式良好，但由于语义错误而无法遵循。", "422 Unprocessable Entity - 请求格式良好，但由于语义错误而无法遵循。"),
    /**
     * 429 Too Many Requests
     * 用户在给定的时间内发送了太多请求（“限制请求速率”）。
     */
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), "429", "服务器繁忙，请稍后重试", "429 Too Many Requests - 该资源限制用户重复提交请求"),

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
