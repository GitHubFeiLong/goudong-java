package com.goudong.commons.exception;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.framework.core.DataMap;
import com.goudong.commons.utils.core.MessageFormatUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;

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

    @ApiModelProperty(value = "返回额外信息")
    public Map dataMap = new HashMap();

    /**
     * 根据异常对象，返回自定义的服务异常对象
     * @param throwable
     * @return
     */
    public static BasicException generateByServer(Throwable throwable) {
        // 默认500异常
        BasicException basicException = ServerException.serverException(ServerExceptionEnum.SERVER_ERROR);

        if (throwable instanceof RuntimeException) {
            return ServerException.serverException(ServerExceptionEnum.SERVER_ERROR, throwable.getMessage(), "RuntimeException " + throwable.getMessage());
        }

        // 空指针
        if (throwable instanceof NullPointerException) {
            return ServerException.serverException(ServerExceptionEnum.SERVER_ERROR, "空指针异常", "NullPointerException null");
        }

        // 上传文件错误
        if (throwable instanceof MultipartException) {
            return ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "上传文件失败", "MultipartException " +throwable.getMessage());
        }

        if (throwable instanceof IndexOutOfBoundsException ) {
            return ServerException.serverException(ServerExceptionEnum.SERVICE_UNAVAILABLE, "");
        }
        String message = throwable.getMessage();
        if (message == null) {
            return basicException;
        }

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
    public static BasicException client(ClientExceptionEnum exceptionEnum) {
        return new BasicException(exceptionEnum);
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum, String clientMessage) {
        return new BasicException(exceptionEnum, clientMessage);
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     */
    public static BasicException client(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        return new BasicException(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param params 模板参数
     */
    public static BasicException clientByTemplate(ClientExceptionEnum exceptionEnum, String clientMessageTemplate, Object... params) {
        String clientMessage = MessageFormatUtil.format(clientMessageTemplate, params);
        return new BasicException(exceptionEnum, clientMessage);
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 模板参数
     * @param serverMessageTemplate 自定义服务提示信息模板
     * @param serverMessageParams 模板参数
     */
    public static BasicException clientByTemplate(ClientExceptionEnum exceptionEnum,
                                                  String clientMessageTemplate,
                                                  Object[] clientMessageParams,
                                                  String serverMessageTemplate,
                                                  Object[] serverMessageParams) {

        return new BasicException(exceptionEnum,
                MessageFormatUtil.format(clientMessageTemplate, clientMessageParams),
                MessageFormatUtil.format(serverMessageTemplate, serverMessageParams));
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum) {
        return new BasicException(exceptionEnum);
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     * @param serverMessage 自定义服务端提示信息
     */
    public static BasicException server(ServerExceptionEnum exceptionEnum, String serverMessage) {
        return new BasicException(exceptionEnum, serverMessage);
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage 自定义服务端提示信息
     */
    public static BasicException server(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        return new BasicException(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 服务端异常
     * @param exceptionEnum
     * @param serverMessageTemplate 自定义服务端提示信息模板
     * @param params 模板参数
     */
    public static BasicException serverByTemplate(ClientExceptionEnum exceptionEnum, String serverMessageTemplate, Object... params) {
        String serverMessage = MessageFormatUtil.format(serverMessageTemplate, params);
        return new BasicException(exceptionEnum, serverMessage);
    }

    /**
     * 客户端误操作造成异常
     * @param exceptionEnum
     * @param clientMessageTemplate 自定义客户端提示信息模板
     * @param clientMessageParams 模板参数
     * @param serverMessageTemplate 自定义服务提示信息模板
     * @param serverMessageParams 模板参数
     */
    public static BasicException serverByTemplate(ServerExceptionEnum exceptionEnum,
                                                  String clientMessageTemplate,
                                                  Object[] clientMessageParams,
                                                  String serverMessageTemplate,
                                                  Object[] serverMessageParams) {

        return new BasicException(exceptionEnum,
                MessageFormatUtil.format(clientMessageTemplate, clientMessageParams),
                MessageFormatUtil.format(serverMessageTemplate, serverMessageParams));
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

    //~ 补充额外信息
    //==================================================================================================================

    public BasicException code(String code) {
        this.code = code;
        return this;
    }

    public BasicException clientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
        return this;
    }

    public BasicException serverMessage(String serverMessage) {
        this.serverMessage = serverMessage;
        return this;
    }

    public BasicException dataMap(Map dataMap) {
        this.dataMap = dataMap;
        return this;
    }
    public BasicException dataMap(DataMap dataMap) {
        this.dataMap = BeanUtil.beanToMap(dataMap, false, true);
        return this;
    }

    public BasicException dataMapPut(Map dataMap) {
        this.dataMap.putAll(dataMap);
        return this;
    }

    public BasicException dataMapPut(DataMap dataMap) {
        this.dataMap.putAll(BeanUtil.beanToMap(dataMap, false, true));
        return this;
    }

    public BasicException dataMapPut(String key, Object value) {
        this.dataMap.put(key, value);
        return this;
    }

    public BasicException dataMapPut(String... kv) {
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
}
