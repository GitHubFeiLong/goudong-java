package com.goudong.commons.pojo;

import com.goudong.commons.exception.BasicException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.aspectj.weaver.ast.Not;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Date;
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
    @ApiModelProperty(value = "自定义状态码", required = true, example = "A0001")
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
    @ApiModelProperty(value = "额外自定义数据", required = true)
    private T data;

    /**
     * 数据
     */
    @ApiModelProperty(value = "扩展额外的数据")
    private Map dataMap;

    /**
     * 时间戳
     */
    @ApiModelProperty(value = "时间戳")
    private Date timestamp;

    public Result() {
    }
    public Result(String code) {
        this.code = code;
        this.timestamp = new Date();
    }
    public Result(String code, String clientMessage, String serverMessage, T t) {
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
        this.data = t;
        this.timestamp = new Date();
    }


    /**
     * 返回成功
     * @return
     */
    public static Result<Object> ofSuccess() {
        return new Result(Result.SUCCESS);
    }
    /**
     * 返回成功,带数据
     * @return
     */
    public static <T> Result<T> ofSuccess(T t) {
        return new Result(Result.SUCCESS, null, null, t);
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
     * 返回失败，带错误信息
     * @return
     */
    public static Result ofFail(BasicException basicException) {
        return  new Result(basicException.getCode(), basicException.getClientMessage(), basicException.getServerMessage(), null);
    }

    /**
     * 404 Not Found
     * @return
     */
    public static Result ofFailByNotFound(String url, Object t) {
        return  new Result("404", "当前请求资源不存在，请稍后再试", HttpStatus.NOT_FOUND.getReasonPhrase() + " - 目标资源资源不存在：" + url, t);
    }

    /**
     * 405 Method Not Allowed
     * @return
     */
    public static Result ofFailByMethodNotAllowed(String url, Object t) {
        return  new Result("405", "当前资源请求方式错误，请稍后再试", HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase() + " - 目标资源资源不存在：" + url, t);
    }

}
