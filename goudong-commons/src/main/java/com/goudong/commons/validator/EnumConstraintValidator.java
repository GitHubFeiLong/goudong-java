package com.goudong.commons.validator;

import com.goudong.commons.annotation.validator.EnumValidator;
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
 *
 * @Author e-Feilong.Chen
 * @Date 2022/2/11 14:46
 */
public class EnumConstraintValidator implements ConstraintValidator<EnumValidator, String> {

    /**
     * 枚举类对象
     */
    private Class<? extends Enum> enumClass;

    @Override
    public void initialize(EnumValidator enumValidator) {
        this.enumClass = enumValidator.enumClass();
    }

    /**
     * 验证是否成功
     * @param value 需要校验的值
     * @param context
     * @return 返回布尔值，当返回true时，表示校验成功；当返回false时，表示校验失败并抛出异常。
     */
    @SneakyThrows
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Object[] values = (Object[])enumClass.getMethod("values").invoke(enumClass);
        Method nameMethod = enumClass.getMethod("name");
        List<String> valueNameList = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            String name = (String)nameMethod.invoke(values[i]);
            valueNameList.add(name);
            // 存在name()与value相等，表名是枚举成员
            if (Objects.equals(name, value)) {
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
