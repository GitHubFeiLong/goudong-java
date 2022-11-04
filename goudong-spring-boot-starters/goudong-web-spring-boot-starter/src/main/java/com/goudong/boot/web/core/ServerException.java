package com.goudong.boot.web.core;

import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 类描述：
 * 服务器内部错误
 * @Author e-Feilong.Chen
 * @Date 2021/8/10 16:13
 */
public class ServerException extends BasicException {
    public static final Logger log = LoggerFactory.getLogger(ServerException.class);

    public static final String clientMessage = "服务器内部错误";

    /**
     * 默认500异常
     */
    public static final ServerExceptionEnum DEFAULT_EXCEPTION = ServerExceptionEnum.SERVER_ERROR;


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
