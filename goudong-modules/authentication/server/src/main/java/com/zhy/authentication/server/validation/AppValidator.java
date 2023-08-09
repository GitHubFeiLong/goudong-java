package com.zhy.authentication.server.validation;

import cn.hutool.extra.spring.SpringUtil;
import com.zhy.authentication.server.service.BaseAppService;
import com.zhy.authentication.server.service.dto.BaseAppDTO;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.*;
import java.util.Objects;
import java.util.Optional;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(
        validatedBy = {AppValidator.AppConstraintValidator.class}
)
@Documented
public @interface AppValidator {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    class AppConstraintValidator implements ConstraintValidator<AppValidator, Long> {

        private static BaseAppService baseAppService;
        public AppConstraintValidator() {
        }

        public boolean isValid(Long value, ConstraintValidatorContext context) {
            BaseAppService service = Optional.ofNullable(baseAppService).orElseGet(() -> baseAppService = SpringUtil.getBean(BaseAppService.class));
            if (value != null) {
                Optional<BaseAppDTO> one = service.findOne(value);
                if (!one.isPresent()) {
                    if (Objects.equals(context.getDefaultConstraintMessageTemplate(), "")) {
                        context.disableDefaultConstraintViolation();
                        context.buildConstraintViolationWithTemplate("应用不存在").addConstraintViolation();
                    }
                    return false;
                }
            }

            return true;
        }
    }
}
