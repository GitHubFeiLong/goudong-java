package com.goudong.commons.enumerate;

import com.goudong.commons.utils.JwtTokenUtil;
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
public enum ClientExceptionEnumInterface implements ExceptionEnumInterface {
    /**
     * 400 Bad Request
     * 1、语义有误，当前请求无法被服务器理解。除非进行修改，否则客户端不应该重复提交这个请求。
     * 2、请求参数有误。
     */
    BAD_REQUEST(400, "400", "参数错误", "Bad Request - 语义有误，当前请求无法被服务器理解。除非进行修改，否则客户端不应该重复提交这个请求;请求参数有误"),

    NOT_AUTHENTICATION(401, "401", "请登录", "用户未登录"),
    AUTHENTICATION_EXPIRES(401, "401001", "登录过期", "登录过期"),
    NOT_AUTHORIZATION(403, "403", "无权访问", "用户没有权限"),
    NAME_OR_PWD_ERROR(400, "400002", "无权访问", "用户名与密码错误"),


    /**
     * 404 Not Found
     * 请求失败，请求所希望得到的资源未被在服务器上发现。没有信息能够告诉用户这个状况到底是暂时的还是永久的。
     * 假如服务器知道情况的话，应当使用410状态码来告知旧资源因为某些内部的配置机制问题，已经永久的不可用，而且没有任何可以跳转的地址。
     * 404这个状态码被广泛应用于当服务器不想揭示到底为何请求被拒绝或者没有其他适合的响应可用的情况下。
     */
    NOT_FOUND(404, "404", "资源不存在", "Not Found - 请求失败，请求所希望得到的资源未被在服务器上发现。"),


    /**
     * 406 Not Acceptable
     * 请求的资源的内容特性无法满足请求头中的条件，因而无法生成响应实体。
     */
    TOKEN_ERROR(406, "406", "请您刷新页面", "Not Acceptable - 缺少请求头 " + JwtTokenUtil.TOKEN_HEADER + "或 token格式错误"),

    /**
     * 429 Too Many Requests
     * 用户在给定的时间内发送了太多请求（“限制请求速率”）。
     */
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS.value(), "429", "服务器繁忙，请稍后重试", "Too Many Requests - 该资源限制用户重复提交请求"),

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

    ClientExceptionEnumInterface(int status, String code, String clientMessage, String serverMessage){
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }
}
