package com.goudong.oauth2.exception;

/**
 * 类描述：
 * 令牌过期异常
 * @author msi
 * @version 1.0
 * @date 2022/1/23 15:59
 */
public class RefreshTokenExpiredException extends TokenException {
    //~fields
    //==================================================================================================================
    public static final String DEFAULT_CLIENT_MESSAGE = "令牌过期";

    //~methods
    //==================================================================================================================
    public RefreshTokenExpiredException(String serverMessage) {
        super(DEFAULT_CLIENT_MESSAGE, serverMessage);
    }

    public RefreshTokenExpiredException(String clientMessage, String serverMessage) {
        super(clientMessage, serverMessage);
    }
}
