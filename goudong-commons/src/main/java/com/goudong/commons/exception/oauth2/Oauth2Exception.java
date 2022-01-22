package com.goudong.commons.exception.oauth2;

import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.exception.BasicException;

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

    /**
     * 服务端异常
     *
     * @param exceptionEnum
     */
    public Oauth2Exception(ServerExceptionEnum exceptionEnum) {
        super(exceptionEnum);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum
     * @param serverMessage 服务端错误信息
     */
    public Oauth2Exception(ServerExceptionEnum exceptionEnum, String serverMessage) {
        super(exceptionEnum, serverMessage);
    }

    /**
     * 服务端异常
     *
     * @param exceptionEnum 状态码相关
     * @param clientMessage 客户端提示
     * @param serverMessage 服务端提示
     */
    public Oauth2Exception(ServerExceptionEnum exceptionEnum, String clientMessage, String serverMessage) {
        super(exceptionEnum, clientMessage, serverMessage);
    }

    /**
     * 构造方法
     *
     * @param status        http状态码
     * @param code          自定义状态码
     * @param clientMessage 客户端显示信息
     * @param serverMessage 服务端日志显示信息
     */
    public Oauth2Exception(int status, String code, String clientMessage, String serverMessage) {
        super(status, code, clientMessage, serverMessage);
    }



}