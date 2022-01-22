package com.goudong.commons.exception.oauth2;

import com.goudong.commons.enumerate.core.ClientExceptionEnum;

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
    /**
     * 客户端误操作造成异常
     *
     * @param clientMessage
     */
    public AccessTokenExpiredException(String clientMessage) {
        super(ClientExceptionEnum.UNAUTHORIZED, clientMessage);
    }



}