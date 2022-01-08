package com.goudong.commons.enumerate.user;

import lombok.Getter;

/**
 * 枚举描述：
 * 用户注册账号时，手机号绑定的用户是否是自己的账号
 * @author msi
 * @version 1.0
 * @date 2022/1/8 11:03
 */
@Getter
public enum AccountRadioEnum {
    /**
     * 没人使用的账号
     */
    BLANK(""),
    /**
     * 自己的账号
     */
    MY_SELF("MY_SELF"),

    /**
     * 不是自己的账号
     */
    NOT_MY_SELF("NOT_MY_SELF"),
    ;

    private String value;

    AccountRadioEnum(String value) {
        this.value = value;
    }
}
