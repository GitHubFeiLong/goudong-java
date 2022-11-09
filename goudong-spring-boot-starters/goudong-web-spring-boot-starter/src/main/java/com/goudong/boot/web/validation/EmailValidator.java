package com.goudong.boot.web.validation;

import com.goudong.core.lang.RegexConst;
import com.goudong.core.util.StringUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 类描述：
 * 校验参数是邮箱格式
 * @author cfl
 * @date 2022/8/28 14:43
 * @version 1.0
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.EmailConstraintValidator.class)
@Documented
public @interface EmailValidator {

    String message() default "";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };


    /**
     * 类描述：
     * 注解{@link EmailValidator} 的底层功能
     * @author cfl
     * @date 2022/8/28 14:44
     * @version 1.0
     */
    class EmailConstraintValidator implements ConstraintValidator<EmailValidator, String> {

        /**
         * 验证是否成功
         * @param value 需要校验的值
         * @param context
         * @return 返回布尔值，当返回true时，表示校验成功；当返回false时，表示校验失败并抛出异常。
         */
        @Override
        @SuppressWarnings(value = "all")
        public boolean isValid(String value, ConstraintValidatorContext context) {
            if (StringUtil.isBlank(value)) {
                //禁用默认的message的值
                context.disableDefaultConstraintViolation();
                //重新添加错误提示语句
                context.buildConstraintViolationWithTemplate("邮箱不能为空")
                        .addConstraintViolation();
                return false;
            }
            if (!Pattern.matches(RegexConst.EMAIL, value)) {
                /*
                    动态设置错误提示语句
                */
                if (Objects.equals(context.getDefaultConstraintMessageTemplate(), "")) {
                    //禁用默认的message的值
                    context.disableDefaultConstraintViolation();
                    //重新添加错误提示语句
                    context.buildConstraintViolationWithTemplate("邮箱格式错误")
                            .addConstraintViolation();
                }

                return false;
            }

            return true;
        }
    }
}



