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
    X_APP_ID_INVALID("10000001"),

    ;

    /**
     * Http响应码
     */
    private int status;

    /**
     * 异常状态码
     */
    private String code;

    ExceptionEnum(String code) {
        this.status = 400;
        this.code = code;
    }

    ExceptionEnum(int status, String code) {
        this.status = status;
        this.code = code;
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
        return "未知异常";
    }

    /**
     * 服务器日志信息
     */
    @Override
    public String getServerMessage() {
        return "未知异常";
    }
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
}
