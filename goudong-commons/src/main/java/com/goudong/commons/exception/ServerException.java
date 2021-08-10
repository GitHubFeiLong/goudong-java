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

    public ServerException(ServerExceptionEnum serverExceptionEnum) {
        super(serverExceptionEnum);
    }

    public static BasicException exception(String serverMessage){
        log.error("服务器内部错误：{}", serverMessage);
        throw new BasicException(500, "500500", clientMessage, serverMessage);
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
}
