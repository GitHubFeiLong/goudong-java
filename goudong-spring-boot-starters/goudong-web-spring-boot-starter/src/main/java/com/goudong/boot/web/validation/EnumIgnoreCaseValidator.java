package com.goudong.boot.web.validation;

import com.goudong.core.util.StringUtil;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

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
@Constraint(validatedBy = EnumIgnoreCaseValidator.EnumIgnoreCaseConstraintValidator.class)
@Documented
public @interface EnumIgnoreCaseValidator {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Enum> enumClass();

    Class<? extends Payload>[] payload() default {};

    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        EnumIgnoreCaseValidator[] value();
    }

    /**
     * 类描述：
     * 忽略大小的枚举验证
     *
     * @author msi
     * @version 1.0
     * @date 2022/2/13 15:06
     */
    class EnumIgnoreCaseConstraintValidator implements ConstraintValidator<EnumIgnoreCaseValidator, String> {

        //~fields
        //==================================================================================================================
        /**
         * 枚举类对象
         */
        private Class<? extends Enum> enumClass;

        //~methods
        //==================================================================================================================
        @Override
        public void initialize(EnumIgnoreCaseValidator enumValidator) {
            this.enumClass = enumValidator.enumClass();
        }

        @Override
        @SuppressWarnings(value = "all")
        public boolean isValid(String value, ConstraintValidatorContext context) {
            try {
                Object[] values = (Object[]) enumClass.getMethod("values").invoke(enumClass);
                Method nameMethod = enumClass.getMethod("name");
                java.util.List<String> valueNameList = new ArrayList<>();
                for (int i = 0; i < values.length; i++) {
                    String name = (String) nameMethod.invoke(values[i]);
                    valueNameList.add(name);
                    // 存在name()与value相等，表名是枚举成员,忽略大小写
                    if (name.equalsIgnoreCase(value)) {
                        return true;
                    }
                }

                /*
                    动态设置错误提示语句
                 */
                if (Objects.equals(context.getDefaultConstraintMessageTemplate(), "")) {
                    //禁用默认的message的值
                    context.disableDefaultConstraintViolation();
                    //重新添加错误提示语句
                    context.buildConstraintViolationWithTemplate(
                                    String.format("%s不是%s的成员(%s)",
                                            value,
                                            enumClass.getSimpleName(),
                                            StringUtil.join(valueNameList, ",")))
                            .addConstraintViolation();
                }

                return false;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
