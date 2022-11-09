package com.goudong.boot.web.enumerate;

import com.goudong.core.lang.RegexConst;
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
    STRICTNESS(RegexConst.PHONE_STRICTNESS),
    /**
     * 宽松
     */
    LOOSE(RegexConst.PHONE_LOOSE),
    /**
     * 最宽松
     */
    MOST_LOOSE(RegexConst.PHONE_MOST_LOOSE),
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
