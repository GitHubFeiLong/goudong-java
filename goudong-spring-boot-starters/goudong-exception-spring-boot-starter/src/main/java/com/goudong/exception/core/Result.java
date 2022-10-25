package com.goudong.exception.core;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.exception.enumerate.ExceptionEnumInterface;
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
public class Result<T> implements Serializable {

    private static final String DEFAULT_SUCCESS_CLIENT_MESSAGE = "执行成功";
    /**
     * 成功
     */
    public static final String SUCCESS = "0";
    /**
     * 失败
     */
    public static final String FAIL = "1";
    private static final long serialVersionUID = -212122772006061476L;

    /**
     * http状态码
     */
    private int status = 200;
    /**
     * 状态码
     */
    private String code = "200";
    /**
     * 客户端状态码对应信息
     */
    private String clientMessage;

    /**
     * 服务器状态码对应信息
     */
    private String serverMessage;

    /**
     * 数据
     */
    private T data;

    /**
     * 数据
     */
    private Map dataMap = new HashMap();

    /**
     * 时间戳
     */
    private Date timestamp = new Date();

    public Result() {
    }

    public Result(int status) {
        this.status = status;
    }

    public Result(int status, String code) {
        this.status = status;
        this.code = code;
    }

    public Result(int status, String code, T data) {
        this.status = status;
        this.code = code;
        this.data = data;
    }

    public Result(int status, String code, String clientMessage) {
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
    }

    public Result(int status, String code, String clientMessage, T data) {
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.data = data;
    }

    public Result(int status, String code, String clientMessage, String serverMessage) {
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }

    public Result(int status, String code, String clientMessage, String serverMessage, T data, Map dataMap) {
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
        this.data = data;
        this.dataMap = dataMap;
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
        return new Result(200, Result.SUCCESS, DEFAULT_SUCCESS_CLIENT_MESSAGE);
    }

    /**
     * 返回成功,带数据
     * @return
     */
    public static <T> Result<T> ofSuccess(T t) {
        return new Result(200, Result.SUCCESS, DEFAULT_SUCCESS_CLIENT_MESSAGE, t);
    }

    /**
     * 返回成功,提示用户信息,并携带一些数据
     * @param clientMessage
     * @param t
     * @param <T>
     * @return
     */
    public static <T> Result<T> ofSuccess(String clientMessage, T t) {
        return new Result(200, Result.SUCCESS, clientMessage, t);
    }

    /**
     * 返回失败
     * @return
     */
    public static Result ofFail() {
        return new Result(500, Result.FAIL);
    }
    /**
     * 返回失败，带数据
     * @return
     */
    public static <T> Result<T> ofFail(T t) {
        return new Result(500, Result.FAIL, t);
    }

    /**
     * 只返回失败信息，不抛额异常
     * @return
     */
    public static Result ofFail(ExceptionEnumInterface enumInterface) {
        return new Result(enumInterface.getStatus(), enumInterface.getCode(), enumInterface.getClientMessage(), enumInterface.getServerMessage());
    }

    /**
     * 只返回失败信息，不抛额异常
     * @return
     */
    public static Result ofFail(BasicException basicException) {
        return new Result(basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage());
    }

    /**
     * 400 Bad Request
     * @param clientMessage 客户端显示错误
     * @param serverMessage 服务端错误
     * @return
     */
    public static Result ofFailByBadRequest(String clientMessage, String serverMessage) {
        return new Result(400, "400", clientMessage, HttpStatus.BAD_REQUEST.getReasonPhrase() + " - " + serverMessage);
    }

    /**
     * 400 Bad Request
     * @param clientMessage 客户端显示错误
     * @param serverMessage 服务端错误
     * @return
     */
    public static Result ofFailByForBidden(String clientMessage, String serverMessage) {
        return new Result(403, "403", clientMessage, HttpStatus.BAD_REQUEST.getReasonPhrase() + " - " + serverMessage);
    }

    /**
     * 404 Not Found
     * @param url 访问的资源地址
     * @return
     */
    public static Result ofFailByNotFound(String url) {
        return new Result(404, "404", "当前请求资源不存在，请稍后再试", HttpStatus.NOT_FOUND.getReasonPhrase() + " - 目标资源资源不存在：" + url);
    }

    /**
     * 405 Method Not Allowed
     * @param url 访问的资源地址
     * @return
     */
    public static Result ofFailByMethodNotAllowed(String url) {
        return new Result(405, "405", "当前资源请求方式错误，请稍后再试", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase() + " - 目标资源资源不存在：" + url);
    }

    /**
     * 406 Method Not Allowed
     * @param serverMessage 异常描述
     * @return
     */
    public static Result ofFailByNotAcceptable(String serverMessage) {
        return new Result(406, "406", "当前请求携带的请求头错误", HttpStatus.NOT_ACCEPTABLE.getReasonPhrase() + " - " + serverMessage);
    }

    // ~ 属性设置
    //==================================================================================================================
    public Result status() {
        this.status = status;
        return this;
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

    public Result dataMap(Map dataMap) {
        this.dataMap = dataMap;
        return this;
    }
    public Result dataMap(AbstractDataMap dataMap) {
        this.dataMap = BeanUtil.beanToMap(dataMap, false, true);
        return this;
    }

    public Result dataMapPut(Map dataMap) {
        this.dataMap.putAll(dataMap);
        return this;
    }

    public Result dataMapPut(AbstractDataMap dataMap) {
        this.dataMap.putAll(BeanUtil.beanToMap(dataMap, false, true));
        return this;
    }

    public Result dataMapPut(String key, Object value) {
        this.dataMap.put(key, value);
        return this;
    }

    public Result dataMapPut(String... kv) {
        // 不是偶数位
        if (kv.length < 2 && kv.length % 2 != 0) {
            throw new IllegalArgumentException("参数kv数组不正确，要是2的倍数，其中奇数是key偶数是value");
        }

        // 步长为2
        for (int i = 0, length = kv.length; i < length; i+=2) {
            String key = kv[i];
            String value = kv[i+1];
            this.dataMap.put(key, value);
        }

        return this;
    }

    public Result timestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }
}
