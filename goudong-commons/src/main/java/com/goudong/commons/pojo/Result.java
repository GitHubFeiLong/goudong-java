package com.goudong.commons.pojo;

import com.goudong.commons.exception.BasicException;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
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


    public Result() {
    }
    public Result(String code) {
        this.code = code;
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



}
