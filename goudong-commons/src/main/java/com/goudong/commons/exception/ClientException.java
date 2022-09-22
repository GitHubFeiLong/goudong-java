package com.goudong.commons.exception;

import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：
 * 客户端内部错误
 * @Author e-Feilong.Chen
 * @Date 2021/8/10 16:12
 */
@Slf4j
public class ClientException extends BasicException {

    /**
     * 默认400异常
     */
    public static final ClientExceptionEnum DEFAULT_EXCEPTION = ClientExceptionEnum.BAD_REQUEST;

    /**
     * 代替 构造方法(用起舒服些)
     * @param clientExceptionEnum 客户端错误枚举
     * @return
     */
    @Deprecated
    public static ClientException clientException (ClientExceptionEnum clientExceptionEnum) {
        return new ClientException(clientExceptionEnum);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param clientExceptionEnum 客户端错误枚举
     * @param clientMessage 动态自定义客户端提示信息
     * @return
     */
    @Deprecated
    public static ClientException clientException (ClientExceptionEnum clientExceptionEnum, String clientMessage) {
        return new ClientException(clientExceptionEnum, clientMessage);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param clientExceptionEnum 客户端错误枚举
     * @param clientMessage 动态自定义客户端提示信息
     * @param serverMessage 动态自定义服务端异常提示信息
     * @return
     */
    @Deprecated
    public static ClientException clientException (ClientExceptionEnum clientExceptionEnum, String clientMessage, String serverMessage) {
        return new ClientException(clientExceptionEnum, clientMessage, serverMessage);
    }

    public ClientException() {
        super(DEFAULT_EXCEPTION);
    }

    public ClientException(String clientMessage) {
        super(DEFAULT_EXCEPTION, clientMessage);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum) {
        super(clientExceptionEnum);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum, String clientMessage) {
        super(clientExceptionEnum, clientMessage);
    }

    public ClientException(ClientExceptionEnum clientExceptionEnum, String clientMessage, String serverMessage) {
        super(clientExceptionEnum, clientMessage, serverMessage);
    }
}
