package com.goudong.commons.annotation.validator;

import com.goudong.commons.validator.EnumConstraintValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 类描述：
 * 校验参数是指定枚举成员，严格大小写。
 * 和{@link EnumIgnoreCaseValidator}相似
 *
 * @auther msi
 * @date 2022/2/11 15:15
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EnumConstraintValidator.class)
@Repeatable(value = EnumValidator.List.class)
@Documented
public @interface EnumValidator {

    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends java.lang.Enum> enumClass();

    Class<? extends Payload>[] payload() default { };

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EnumValidator[] value();
    }
}



