package com.goudong.core.lang;

import java.io.Serializable;
import java.util.*;

/**
 * 类描述：
 * 统一API响应结果封装
 * @author msi
 */
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 3269921052498851769L;

    protected static final String DEFAULT_SUCCESS_CLIENT_MESSAGE = "执行成功";
    /**
     * 成功
     */
    public static final String SUCCESS = "0";
    /**
     * 失败
     */
    public static final String FAIL = "1";

    /**
     * http状态码
     */
    private int status = 200;
    /**
     * 状态码,“0”代表成功，非“0”代表失败
     * <pre>
     *     {@code code = 0}     成功
     *     {@code code != 0}    失败
     * </pre>
     */
    private String code = "0";
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
     * 额外数据
     */
    private Map<Object, Object> dataMap;

    /**
     * 属性，用于额外操作
     */
    private List<String> properties;

    /**
     * 时间戳
     */
    private Date timestamp = new Date();

    public Result() {
    }

    public Result(int status) {
        this.status = status;
        this.code = String.valueOf(status);
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

    public Result(int status, String code, String clientMessage, String serverMessage, T data, Map<Object, Object> dataMap) {
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
     * @return result
     */
    public static Result<Object> ofSuccess() {
        return new Result<>(200, Result.SUCCESS, DEFAULT_SUCCESS_CLIENT_MESSAGE);
    }

    /**
     * 返回成功,带数据
     * @return  result
     */
    public static <T> Result<T> ofSuccess(T t) {
        return new Result<>(200, Result.SUCCESS, DEFAULT_SUCCESS_CLIENT_MESSAGE, t);
    }

    /**
     * 返回成功,提示用户信息,并携带一些数据
     * @param clientMessage 客户端提示信息
     * @param t data
     * @return  result
     */
    public static <T> Result<T> ofSuccess(String clientMessage, T t) {
        return new Result<>(200, Result.SUCCESS, clientMessage, t);
    }

    /**
     * 返回失败
     * @return  result
     */
    public static Result<Object> ofFail() {
        return new Result<>(500, Result.FAIL);
    }
    /**
     * 返回失败，带数据
     * @return  result
     */
    public static <T> Result<T> ofFail(T t) {
        return new Result<>(500, Result.FAIL, t);
    }

    /**
     * 使用 BasicExceptionInterface 转换成响应对象
     * @param basicException 异常
     * @return  result
     */
    public static Result<Object> ofFail(BasicExceptionInterface basicException) {
        return new Result<>(basicException.getStatus(), basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage());
    }

    /**
     * 只返回失败信息，不抛额异常
     * @param enumInterface 异常枚举
     * @return  result
     */
    public static Result<Object> ofFail(ExceptionEnumInterface enumInterface) {
        return new Result<>(enumInterface.getStatus(), enumInterface.getCode(), enumInterface.getClientMessage(), enumInterface.getServerMessage());
    }

    /**
     * 400 Bad Request
     * @param clientMessage 客户端显示错误
     * @param serverMessage 服务端错误
     * @return  result
     */
    public static Result<Object> ofFailByBadRequest(String clientMessage, String serverMessage) {
        return new Result<>(400, "400", clientMessage, "Bad Request - " + serverMessage);
    }

    /**
     * 400 Bad Request
     * @param clientMessage 客户端显示错误
     * @param serverMessage 服务端错误
     * @return  result
     */
    public static Result<Object> ofFailByForBidden(String clientMessage, String serverMessage) {
        return new Result<>(403, "403", clientMessage, "Forbidden - " + serverMessage);
    }

    /**
     * 404 Not Found
     * @param url 访问的资源地址
     * @return  result
     */
    public static Result<Object> ofFailByNotFound(String url) {
        return new Result<>(404, "404", "当前请求资源不存在，请稍后再试", "Not Found - 目标资源资源不存在：" + url);
    }

    /**
     * 405 Method Not Allowed
     * @param url 访问的资源地址
     * @return result
     */
    public static Result<Object> ofFailByMethodNotAllowed(String url) {
        return new Result<>(405, "405", "当前资源请求方式错误，请稍后再试", "Method Not Allowed - 目标资源资源不存在：" + url);
    }

    /**
     * 406 Method Not Allowed
     * @param serverMessage 异常描述
     * @return result
     */
    public static Result<Object> ofFailByNotAcceptable(String serverMessage) {
        return new Result<>(406, "406", "当前请求携带的请求头错误", "Not Acceptable - " + serverMessage);
    }

    // ~ 属性设置
    //==================================================================================================================
    /**
     * 设置status
     * @param status   status
     * @return  result
     */
    public Result<T> status(int status) {
        this.status = status;
        return this;
    }

    /**
     * 设置code
     * @param code   code
     * @return  result
     */
    public Result<T> code(String code) {
        this.code = code;
        return this;
    }

    /**
     * 设置clientMessage
     * @param clientMessage   clientMessage
     * @return  result
     */
    public Result<T> clientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
        return this;
    }

    /**
     * 设置serverMessage
     * @param serverMessage   serverMessage
     * @return  result
     */
    public Result<T> serverMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        return this;
    }

    /**
     * 设置data
     * @param data   data
     * @return  result
     */
    public Result<T> data(T data) {
        this.data = data;
        return this;
    }

    /**
     * 设置map
     * @param dataMap   map
     * @return  result
     */
    public Result<T> dataMap(Map<Object, Object> dataMap) {
        this.dataMap = dataMap;
        return this;
    }

    /**
     * 添加map
     * @param dataMap   map
     * @return  result
     */
    public Result<T> dataMapPut(Map<Object, Object> dataMap) {
        Map<Object, Object> map = Optional.ofNullable(this.dataMap).orElseGet(() -> {
            this.dataMap = new HashMap<>();
            return this.dataMap;
        });
        map.putAll(dataMap);
        return this;
    }

    /**
     * 添加key，value
     * @param key   key
     * @param value value
     * @return result
     */
    public Result<T> dataMapPut(String key, Object value) {
        Map<Object, Object> map = Optional.ofNullable(this.dataMap).orElseGet(() -> {
            this.dataMap = new HashMap<>();
            return this.dataMap;
        });
        map.put(key, value);
        return this;
    }

    /**
     * 批量添加key，value
     * @param kv    kv集合
     * @return result
     */
    public Result<T> dataMapPut(String... kv) {
        // 不是偶数位
        if (kv.length < 2 && kv.length % 2 != 0) {
            throw new IllegalArgumentException("参数kv数组不正确，要是2的倍数，其中奇数是key偶数是value");
        }

        Map map = Optional.ofNullable(this.dataMap).orElseGet(() -> {
            this.dataMap = new HashMap<>(kv.length / 2);
            return this.dataMap;
        });

        // 步长为2
        for (int i = 0, length = kv.length; i < length; i+=2) {
            String key = kv[i];
            String value = kv[i+1];
            map.put(key, value);
        }

        return this;
    }

    /**
     * 批量添加key，值为null
     * @param keys  key
     * @return result
     */
    public Result<T> dataMapPutKeys(String... keys) {
        Map<Object, Object> map = Optional.ofNullable(this.dataMap).orElseGet(() -> {
            this.dataMap = new HashMap<>(keys.length);
            return this.dataMap;
        });

        for (String key : keys) {
            map.put(key, null);
        }

        return this;
    }

    /**
     * 设置属性
     * @param properties    属性集合
     * @return  result
     */
    public Result<T> properties(List<String> properties) {
        this.properties = properties;
        return this;
    }

    /**
     * 添加属性
     * @param property  属性
     * @return result
     */
    public Result<T> properties(String property) {
        Optional.ofNullable(this.properties).orElseGet(() -> {
            this.properties = new ArrayList<>();
            return this.properties;
        });
        this.properties.add(property);
        return this;
    }

    /**
     * 设置属性
     * @param properties    属性
     * @return result
     */
    public Result<T> properties(String... properties) {
        Optional.ofNullable(this.properties).orElseGet(() -> {
            this.properties = new ArrayList<>(properties.length);
            return this.properties;
        });

        this.properties.addAll(Arrays.asList(properties));
        return this;
    }

    /**
     * 设置时间戳
     * @param timestamp 时间戳
     * @return result
     */
    public Result<T> timestamp(Date timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }

    public String getServerMessage() {
        return serverMessage;
    }

    public void setServerMessage(String serverMessage) {
        this.serverMessage = serverMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Map getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map dataMap) {
        this.dataMap = dataMap;
    }

    public List<String> getProperties() {
        return properties;
    }

    public void setProperties(List<String> properties) {
        this.properties = properties;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Result<?> result = (Result<?>) o;
        return status == result.status && Objects.equals(code, result.code) && Objects.equals(clientMessage, result.clientMessage) && Objects.equals(serverMessage, result.serverMessage) && Objects.equals(data, result.data) && Objects.equals(dataMap, result.dataMap) && Objects.equals(properties, result.properties) && Objects.equals(timestamp, result.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, code, clientMessage, serverMessage, data, dataMap, properties, timestamp);
    }

    @Override
    public String toString() {
        return "Result{" +
                "status=" + status +
                ", code='" + code + '\'' +
                ", clientMessage='" + clientMessage + '\'' +
                ", serverMessage='" + serverMessage + '\'' +
                ", data=" + data +
                ", dataMap=" + dataMap +
                ", properties=" + properties +
                ", timestamp=" + timestamp +
                '}';
    }
}
