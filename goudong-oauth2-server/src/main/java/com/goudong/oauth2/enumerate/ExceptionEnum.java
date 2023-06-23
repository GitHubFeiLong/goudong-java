package com.goudong.oauth2.enumerate;

import com.goudong.core.lang.ExceptionEnumInterface;

/**
 * 枚举描述：
 * Oauth2 服务的统一异常枚举
 * @author Administrator
 * @version 1.0
 * @date 2023/6/23 13:52
 */
public enum ExceptionEnum implements ExceptionEnumInterface {

    //~app异常
    //==================================================================================================================
    /**
     * 应用通用异常
     */
    APP("10001001"),
    /**
     * 请求头X-App-Id无效
     */
    X_APP_ID_INVALID("10001002", "请求头X-App-Id值无效"),

    /**
     * 应用唯一
     */
    APP_UNIQUE("10001003", "应用已存在"),

    /**
     * 应用不存在
     */
    APP_INVALID("10001004", "应用不存在"),


    ;

    /**
     * Http响应码
     */
    private int status;

    /**
     * 异常状态码
     */
    private String code;

    /**
     * 提示信息
     */
    private String clientMessage;

    ExceptionEnum(String code) {
        this.status = 400;
        this.code = code;
    }

    ExceptionEnum(String code, String clientMessage) {
        this.status = 400;
        this.code = code;
        this.clientMessage = clientMessage;
    }
    ExceptionEnum(int status, String code, String clientMessage) {
        this.status = status;
        this.code = code;
        this.clientMessage = clientMessage;
    }



    /**
     * 响应码
     */
    @Override
    public int getStatus() {
        return this.status;
    }

    /**
     * 错误代码
     */
    @Override
    public String getCode() {
        return this.code;
    }

    /**
     * 客户看见的提示信息
     */
    @Override
    public String getClientMessage() {
        return this.clientMessage;
    }

    /**
     * 服务器日志信息
     */
    @Override
    public String getServerMessage() {
        return null;
    }
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
}
