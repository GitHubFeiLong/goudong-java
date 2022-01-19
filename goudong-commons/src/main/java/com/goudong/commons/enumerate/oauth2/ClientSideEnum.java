package com.goudong.commons.enumerate.oauth2;

import lombok.Getter;

/**
 * 枚举描述：
 * 客户端类型，包含浏览器，app，小程序等
 * @author msi
 * @version 1.0
 * @date 2022/1/19 19:51
 */
@Getter
public enum ClientSideEnum {
    /**
     * 浏览器
     */
    BROWSER("browser"),
    /**
     * app
     */
    APP("app"),
    ;
    //~fields
    //==================================================================================================================
    private String headerValue;

    //~methods
    //==================================================================================================================

    ClientSideEnum(String headerValue) {
        this.headerValue = headerValue;
    }
}
