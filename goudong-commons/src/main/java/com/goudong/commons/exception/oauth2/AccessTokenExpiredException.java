package com.goudong.commons.exception.oauth2;


import com.goudong.boot.exception.enumerate.ClientExceptionEnum;

/**
 * 类描述：
 * 访问令牌过期
 * @author msi
 * @version 1.0
 * @date 2022/1/23 5:09
 */
public class AccessTokenExpiredException extends Oauth2Exception{
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public AccessTokenExpiredException(String clientMessage) {
        super(ClientExceptionEnum.UNAUTHORIZED, clientMessage);
    }

    public AccessTokenExpiredException(String clientMessage, String serverMessage) {
        super(ClientExceptionEnum.UNAUTHORIZED, clientMessage, serverMessage);
    }



}
