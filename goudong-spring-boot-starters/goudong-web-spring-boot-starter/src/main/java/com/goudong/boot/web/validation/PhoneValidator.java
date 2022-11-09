package com.goudong.boot.web.validation;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.goudong.boot.web.enumerate.PhoneValidatorComplexEnum;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 类描述：
 * 校验参数是手机号格式
 * @author cfl
 * @date 2022/8/28 14:43
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PhoneValidator.PhoneConstraintValidator.class)
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

    /**
     * 类描述：
     * 底层功能
     * @author cfl
     * @date 2022/8/28 14:44
     * @version 1.0
     */
    class PhoneConstraintValidator implements ConstraintValidator<PhoneValidator, String> {

        /**
         * 校验的难易程度
         */
        private PhoneValidatorComplexEnum complexEnum;

        /**
         * 初始化
         * @param phoneValidator annotation instance for a given constraint declaration
         */
        @Override
        public void initialize(PhoneValidator phoneValidator) {
            this.complexEnum = phoneValidator.complex();
        }

        /**
         * 验证是否成功
         * @param value 需要校验的值
         * @param context
         * @return 返回布尔值，当返回true时，表示校验成功；当返回false时，表示校验失败并抛出异常。
         */
        @Override
        @SuppressWarnings(value = "all")
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtils.isBlank(value)) {
                //禁用默认的message的值
                context.disableDefaultConstraintViolation();
                //重新添加错误提示语句
                context.buildConstraintViolationWithTemplate("手机号格式错误")
                        .addConstraintViolation();
                return false;
            }
            if (!Pattern.matches(complexEnum.getRegularExpression(), value)) {
                /*
                    动态设置错误提示语句
                */
                if (Objects.equals(context.getDefaultConstraintMessageTemplate(), "")) {
                    //禁用默认的message的值
                    context.disableDefaultConstraintViolation();
                    //重新添加错误提示语句
                    context.buildConstraintViolationWithTemplate("手机号格式错误")
                            .addConstraintViolation();
                }

                return false;
            }

            return true;
        }
    }
}



