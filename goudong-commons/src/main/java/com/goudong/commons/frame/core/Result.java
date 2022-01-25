package com.goudong.commons.frame.core;

import com.goudong.commons.enumerate.core.ExceptionEnumInterface;
import com.goudong.commons.exception.BasicException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 类描述：
 *  统一API响应结果封装
 * @ClassName Result
 * @Author msi
 * @Date 2020/10/5 18:42
 * @Version 1.0
 */
@Data
@ApiModel(value = "Result", description = "统一结果返回结构封装类")
public class Result<T> implements Serializable {

    private static final String DEFAULT_SUCCESS_CLIENT_MESSAGE = "执行成功";
    /**
     * 成功
     */
    public static final String SUCCESS = "1";
    /**
     * 失败
     */
    public static final String FAIL = "0";
    private static final long serialVersionUID = -212122772006061476L;

    /**
     * 状态码
     */
    @ApiModelProperty(value = "响应码", required = true, example = "404")
    private String code;
    /**
     * 客户端状态码对应信息
     */
    @ApiModelProperty(value = "状态码对应描述", required = true, example = "用户不存在")
    private String clientMessage;

    /**
     * 服务器状态码对应信息
     */
    @ApiModelProperty(value = "状态码对应描述", required = true, example = "用户不存在")
    private String serverMessage;

    /**
     * 数据
     */
    @ApiModelProperty(value = "额外自定义数据")
    private T data;

    /**
     * 数据
     */
    @ApiModelProperty(value = "扩展额外的数据")
    private Map dataMap = new HashMap();

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private Date timestamp = new Date();

    public Result() {
    }

    public Result(String code) {
        this.code = code;
    }

    public Result(String code, String clientMessage, String serverMessage) {
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }
    public Result(String code, String clientMessage, String serverMessage, T t) {
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
        this.data = t;
    }

    /**
     * 返回成功
     * @return
     */
    public static Result<Object> ofSuccess() {
        return new Result(Result.SUCCESS).clientMessage(DEFAULT_SUCCESS_CLIENT_MESSAGE);
    }

    /**
     * 返回成功,带数据
     * @return
     */
    public static <T> Result<T> ofSuccess(T t) {
        return new Result(Result.SUCCESS, DEFAULT_SUCCESS_CLIENT_MESSAGE, null, t);
    }

    /**
     * 返回成功,提示用户信息,并携带一些数据
     * @param clientMessage
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Result<T> ofSuccess(String clientMessage, T t) {
        return new Result(Result.SUCCESS, clientMessage, null, t);
    }

    /**
     * 返回失败
     * @return
     */
    public static Result ofFail() {
        return new Result(Result.FAIL);
    }
    /**
     * 返回失败，带数据
     * @return
     */
    public static <T> Result<T> ofFail(T t) {
        return new Result(Result.FAIL, null, null, t);
    }

    /**
     * 只返回失败信息，不抛额异常
     * @return
     */
    public static Result ofFail(ExceptionEnumInterface enumInterface) {
        return new Result(enumInterface.getCode(), enumInterface.getClientMessage(), enumInterface.getServerMessage(), null);
    }

    /**
     * 只返回失败信息，不抛额异常
     * @return
     */
    public static Result ofFail(BasicException basicException) {
        return new Result(basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), null);
    }

    /**
     * 400 Bad Request
     * @param clientMessage 客户端显示错误
     * @param serverMessage 服务端错误
     * @return
     */
    public static Result ofFailByBadRequest(String clientMessage, String serverMessage) {
        return new Result("400", clientMessage, HttpStatus.BAD_REQUEST.getReasonPhrase() + " - " + serverMessage);
    }

    /**
     * 400 Bad Request
     * @param clientMessage 客户端显示错误
     * @param serverMessage 服务端错误
     * @return
     */
    public static Result ofFailByForBidden(String clientMessage, String serverMessage) {
        return new Result("403", clientMessage, HttpStatus.BAD_REQUEST.getReasonPhrase() + " - " + serverMessage);
    }

    /**
     * 404 Not Found
     * @param url 访问的资源地址
     * @return
     */
    public static Result ofFailByNotFound(String url) {
        return new Result("404", "当前请求资源不存在，请稍后再试", HttpStatus.NOT_FOUND.getReasonPhrase() + " - 目标资源资源不存在：" + url);
    }

    /**
     * 405 Method Not Allowed
     * @param url 访问的资源地址
     * @return
     */
    public static Result ofFailByMethodNotAllowed(String url) {
        return new Result("405", "当前资源请求方式错误，请稍后再试", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase() + " - 目标资源资源不存在：" + url);
    }

    /**
     * 406 Method Not Allowed
     * @param serverMessage 异常描述
     * @return
     */
    public static Result ofFailByNotAcceptable(String serverMessage) {
        return new Result("406", "当前请求携带的请求头错误", HttpStatus.NOT_ACCEPTABLE.getReasonPhrase() + " - " + serverMessage);
    }


    public Result code(String code) {
        this.code = code;
        return this;
    }

    public Result clientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
        return this;
    }

    public Result serverMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        return this;
    }

    public Result data(T data) {
        this.data = data;
        return this;
    }

    public Result data(Map dataMap) {
        this.dataMap = dataMap;
        return this;
    }

    public Result timestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
