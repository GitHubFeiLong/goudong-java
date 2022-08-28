package com.goudong.commons.annotation.validator;

import com.goudong.commons.enumerate.core.PhoneValidatorComplexEnum;
import com.goudong.commons.validator.PhoneConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 类描述：
 * 校验参数是手机号格式
 * @author cfl
 * @date 2022/8/28 14:43
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneConstraintValidator.class)
@Documented
public @interface PhoneValidator {

    String message() default "";

    /**
     * 默认使用严谨的格式进行手机号校验
     * @return
     */
    PhoneValidatorComplexEnum complex() default PhoneValidatorComplexEnum.STRICTNESS;

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}



