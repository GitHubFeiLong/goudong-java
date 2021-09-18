package com.goudong.commons.exception;

import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.enumerate.ServerExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：
 * 自定义异常的基类，其它模块的异常继承进行扩展
 * @ClassName BaseicException
 * @Author msi
 * @Date 2020/6/10 19:41
 * @Version 1.0
 */
@Slf4j
@Getter
@Setter
@ApiModel(value = "BasicException", description = "出现异常，返回的消息")
public class BasicException extends RuntimeException{
    /**
     * http 响应码
     */
    public int status;
    /**
     * 错误代码
     */
    public String code;
    /**
     * 客户端状态码对应信息
     */
    @ApiModelProperty(value = "状态码对应描述", required = true, example = "用户不存在")
    public String clientMessage;

    /**
     * 服务器状态码对应信息
     */
    @ApiModelProperty(value = "状态码对应描述", required = true, example = "用户不存在")
    public String serverMessage;

    /**
     * 根据异常对象，返回自定义的服务异常对象
     * @param throwable
     * @return
     */
    public static BasicException generateByServer(Throwable throwable) {
        // 默认500异常
        BasicException basicException = ServerException.serverException(ServerExceptionEnum.SERVER_ERROR);
        String message = throwable.getMessage();

        // openFeign调用远程服务，服务还未注册到nacos中
        if (message.startsWith("com.netflix.client.ClientException")) {
            return ServerException.serverException(ServerExceptionEnum.SERVICE_UNAVAILABLE, message);
        }
        // // 服务器还未注册到注册中心，网关调用报错
        // if (message.startsWith()) {
        //
        // }

        return basicException;
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     */
    public BasicException(ClientExceptionEnum exceptionEnum) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), exceptionEnum.getServerMessage());
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public BasicException(ClientExceptionEnum exceptionEnum, String clientMessage) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, exceptionEnum.getServerMessage());
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public BasicException(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, serverMessage);
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     */
    public BasicException(ServerExceptionEnum exceptionEnum) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), exceptionEnum.getServerMessage());
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     * @param serverMessage 服务端错误信息
     */
    public BasicException(ServerExceptionEnum exceptionEnum, String serverMessage) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), exceptionEnum.getClientMessage(), serverMessage);
    }

    /**
     * 服务端异常
     * @param exceptionEnum 状态码相关
     * @param clientMessage 客户端提示
     * @param serverMessage 服务端提示
     * @param serverMessage 服务端错误信息
     */
    public BasicException(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        this(exceptionEnum.getStatus(), exceptionEnum.getCode(), clientMessage, serverMessage);
    }

    /**
     * 构造方法
     * @param status http状态码
     * @param code 自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     */
    public BasicException(int status, String code, String clientMessage, String serverMessage) {
        super(clientMessage+"\t"+serverMessage);
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
        this.serverMessage = serverMessage;
    }
}
