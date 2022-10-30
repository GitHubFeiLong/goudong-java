package com.goudong.oauth2.exception;


import com.goudong.boot.web.core.BasicException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;

/**
 * 类描述：
 * 认证服务器异常
 * @author msi
 * @version 1.0
 * @date 2022/1/23 5:09
 */
public class Oauth2Exception extends BasicException {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     */
    public Oauth2Exception(ClientExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }

    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     */
    public Oauth2Exception(ClientExceptionEnum exceptionEnum, String clientMessage) {
        super(exceptionEnum, clientMessage);
    }

    /**
     * 客户端误操作造成异常
     *
     * @param exceptionEnum
     * @param clientMessage 自定义客户端提示信息
     * @param serverMessage
     */
    public Oauth2Exception(ClientExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        super(exceptionEnum, clientMessage, serverMessage);
    }
}
