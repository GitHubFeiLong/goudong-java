package com.goudong.commons.validator;

import com.goudong.commons.annotation.validator.EnumIgnoreCaseValidator;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 忽略大小的枚举验证
 * @author msi
 * @version 1.0
 * @date 2022/2/13 15:06
 */
public class EnumIgnoreCaseConstraintValidator implements ConstraintValidator<EnumIgnoreCaseValidator, String> {

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

    @SneakyThrows
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Object[] values = (Object[])enumClass.getMethod("values").invoke(enumClass);
        Method nameMethod = enumClass.getMethod("name");
        List<String> valueNameList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            String name = (String)nameMethod.invoke(values[i]);
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
                                    StringUtils.join(valueNameList, ",")))
                    .addConstraintViolation();
        }

        return false;
    }


}