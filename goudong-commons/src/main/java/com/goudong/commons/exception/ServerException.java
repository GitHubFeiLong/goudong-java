package com.goudong.commons.exception;

import com.goudong.commons.enumerate.ServerExceptionEnum;
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
     * 服务器内部错误，自己抛出的500异常
     * @param serverMessage 错误描述
     * @return
     */
    public static ServerException serverException (String serverMessage) {
        return new ServerException(ServerExceptionEnum.SERVER_ERROR, serverMessage);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param serverExceptionEnum
     * @return
     */
    public static ServerException serverException (ServerExceptionEnum serverExceptionEnum) {
        return new ServerException(serverExceptionEnum);
    }

    /**
     * 代替 构造方法(用起舒服些)
     * @param serverExceptionEnum
     * @param serverMessage 错误信息
     * @return
     */
    public static ServerException serverException (ServerExceptionEnum serverExceptionEnum, String serverMessage) {
        return new ServerException(serverExceptionEnum, serverMessage);
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

    public ServerException(ServerExceptionEnum serverExceptionEnum) {
        super(serverExceptionEnum);
    }

    public ServerException(ServerExceptionEnum serverExceptionEnum, String serverMessage) {
        super(serverExceptionEnum, serverMessage);
    }

}
