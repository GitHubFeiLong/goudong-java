package com.goudong.oauth2.exception;

import com.goudong.boot.web.enumerate.ClientExceptionEnum;

/**
 * 类描述：
 * 令牌异常
 * @author msi
 * @date 2022/1/23 15:58
 * @version 1.0
 */
public class TokenException extends Oauth2Exception {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    public TokenException(String clientMessage) {
        super(ClientExceptionEnum.UNAUTHORIZED, clientMessage);
    }

    public TokenException(String clientMessage, String serverMessage) {
        super(ClientExceptionEnum.UNAUTHORIZED, clientMessage, serverMessage);
    }



}
