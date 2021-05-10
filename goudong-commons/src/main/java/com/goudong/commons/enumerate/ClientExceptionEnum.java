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
public enum ClientExceptionEnum {


    PARAMETER_ERROR(400, "400400", "参数错误", "请求参数未满足validation注解"),

    NOT_AUTHENTICATION(401, "401000", "请登录", "用户未登录"),
    AUTHENTICATION_EXPIRES(401, "401001", "登录过期", "登录过期"),
    NOT_AUTHORIZATION(403, "403001", "无权访问", "用户没有权限"),
    NAME_OR_PWD_ERROR(400, "400002", "无权访问", "用户名与密码错误"),

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

    ClientExceptionEnum(int status, String code, String clientMessage, String serverMessage){
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }
}
