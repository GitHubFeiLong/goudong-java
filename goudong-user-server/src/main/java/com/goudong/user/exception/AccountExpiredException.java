package com.goudong.user.exception;

import com.goudong.boot.exception.enumerate.ClientExceptionEnum;

/**
 * 类描述：
 * 账户过期异常
 * @author msi
 * @version 1.0
 * @date 2022/1/22 23:13
 */
public class AccountExpiredException extends UserException {

    //~fields
    //==================================================================================================================
    public static final String DEFAULT_CLIENT_MESSAGE = "账户已过期";
    //~methods
    //==================================================================================================================

    /**
     * 默认提示信息
     */
    public AccountExpiredException() {
        super(ClientExceptionEnum.UNAUTHORIZED, DEFAULT_CLIENT_MESSAGE);

    }

    /**
     * 自定义提示信息
     * @param clientMessage
     */
    public AccountExpiredException(String clientMessage) {
        super(ClientExceptionEnum.UNAUTHORIZED, clientMessage);

    }
}
