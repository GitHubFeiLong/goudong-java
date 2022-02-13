package com.goudong.commons.annotation.validator;

import com.goudong.commons.validator.EnumIgnoreCaseConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 类描述：
 * 和{@link EnumValidator}类似，但是本注解不限制大小写
 * @see EnumValidator
 * @auther msi
 * @date 2022/2/11 17:42
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(value = EnumIgnoreCaseValidator.List.class)
@Constraint(validatedBy = EnumIgnoreCaseConstraintValidator.class)
@Documented
public @interface EnumIgnoreCaseValidator {
    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends java.lang.Enum> enumClass();

    Class<? extends Payload>[] payload() default { };

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EnumIgnoreCaseValidator[] value();
    }
}
