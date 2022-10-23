package com.goudong.commons.exception;

import com.goudong.commons.exception.enumerate.ServerExceptionEnum;
import lombok.extern.slf4j.Slf4j;

/**
 * 类描述：
 * 服务器内部错误
 * @Author e-Feilong.Chen
 * @Date 2021/8/10 16:13
 */
@Slf4j
public class ServerException extends BasicException{
    public static final String clientMessage = "服务器内部错误";

    /**
     * 默认500异常
     */
    public static final ServerExceptionEnum DEFAULT_EXCEPTION = ServerExceptionEnum.SERVER_ERROR;

    /**
     * 服务器内部错误，自己抛出的500异常
     * @param serverMessage 错误描述
     * @return
     */
    @Deprecated
    public static ServerException serverException (String serverMessage) {
        return new ServerException(ServerExceptionEnum.SERVER_ERROR, serverMessage);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param serverExceptionEnum
     * @return
     */
    @Deprecated
    public static ServerException serverException (ServerExceptionEnum serverExceptionEnum) {
        return new ServerException(serverExceptionEnum);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param serverExceptionEnum
     * @param serverMessage 错误信息
     * @return
     */
    @Deprecated
    public static ServerException serverException (ServerExceptionEnum serverExceptionEnum, String serverMessage) {
        return new ServerException(serverExceptionEnum, serverMessage);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param serverExceptionEnum
     * @param clientMessage 客户端错误信息
     * @param serverMessage 服务端错误信息
     * @return
     */
    @Deprecated
    public static ServerException serverException (ServerExceptionEnum serverExceptionEnum, String clientMessage, String serverMessage) {
        return new ServerException(serverExceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 服务之间方法调用 参数错误
     * @param serverMessage
     * @return
     */
    public static BasicException methodParamError(String serverMessage){
        log.error("服务器内部方法传参错误：{}", serverMessage);
        throw new BasicException(400, "500400", clientMessage, serverMessage);
    }

    public ServerException() {
        super(ServerException.DEFAULT_EXCEPTION);
    }

    public ServerException(String serverMessage) {
        super(ServerException.DEFAULT_EXCEPTION, serverMessage);
    }
    public ServerException(ServerExceptionEnum serverExceptionEnum) {
        super(serverExceptionEnum);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum, String serverMessage) {
        super(serverExceptionEnum, serverMessage);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum, String clientMessage, String serverMessage) {
        super(serverExceptionEnum, clientMessage, serverMessage);
    }

}
