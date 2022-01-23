package com.goudong.commons.exception.oauth2;

/**
 * 类描述：
 * 刷新令牌无效
 * @author msi
 * @version 1.0
 * @date 2022/1/23 15:59
 */
public class RefreshTokenInvalidException extends TokenException{
    //~fields
    //==================================================================================================================
    public static final String DEFAULT_CLIENT_MESSAGE = "令牌无效";

    //~methods
    //==================================================================================================================
    public RefreshTokenInvalidException(String serverMessage) {
        super(DEFAULT_CLIENT_MESSAGE, serverMessage);
    }
}