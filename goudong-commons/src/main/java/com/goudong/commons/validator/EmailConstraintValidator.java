package com.goudong.commons.validator;

import com.goudong.commons.annotation.validator.EmailValidator;
import com.goudong.commons.utils.core.AssertUtil;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Pattern;

/**
 * 类描述：
 * 注解{@link com.goudong.commons.annotation.validator.EmailValidator} 的底层功能
 * @author cfl
 * @date 2022/8/28 14:44
 * @version 1.0
 */
public class EmailConstraintValidator implements ConstraintValidator<EmailValidator, String> {

    /**
     * 验证是否成功
     * @param value 需要校验的值
     * @param context
     * @return 返回布尔值，当返回true时，表示校验成功；当返回false时，表示校验失败并抛出异常。
     */
    @SneakyThrows
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (StringUtils.isBlank(value)) {
            //禁用默认的message的值
            context.disableDefaultConstraintViolation();
            //重新添加错误提示语句
            context.buildConstraintViolationWithTemplate("邮箱不能为空")
                    .addConstraintViolation();
            return false;
        }
        if (!Pattern.matches(AssertUtil.EMAIL_REGEX, value)) {
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
