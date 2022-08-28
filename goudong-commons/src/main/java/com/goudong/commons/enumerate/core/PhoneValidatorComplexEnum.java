package com.goudong.commons.enumerate.core;

import lombok.Getter;

/**
 * 枚举描述：
 * 手机号校验的难易程度
 * @author Administrator
 * @version 1.0
 * @date 2022/8/28 14:58
 */
@Getter
public enum PhoneValidatorComplexEnum {
    //~fields
    //==================================================================================================================
    /**
     * 严谨
     */
    STRICTNESS("^(?:(?:\\+|00)86)?1(?:(?:3[\\d])|(?:4[5-7|9])|(?:5[0-3|5-9])|(?:6[5-7])|(?:7[0-8])|(?:8[\\d])|(?:9[1|8|9]))\\d{8}$"),
    /**
     * 宽松
     */
    LOOSE("^(?:(?:\\+|00)86)?1[3-9]\\d{9}$"),
    /**
     * 最宽松
     */
    MOST_LOOSE("^(?:(?:\\+|00)86)?1[3-9]\\d{9}$"),
    ;
    //~construct methods
    //==================================================================================================================
    PhoneValidatorComplexEnum(String regularExpression) {
        this.regularExpression = regularExpression;
    }

    //~methods
    //==================================================================================================================
    private String regularExpression;
}
