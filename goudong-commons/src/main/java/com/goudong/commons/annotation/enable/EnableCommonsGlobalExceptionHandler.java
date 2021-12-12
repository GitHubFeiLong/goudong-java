package com.goudong.commons.annotation.enable;

import com.goudong.commons.config.GlobalExceptionConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 开启commons模块的全局异常
 * @author msi
 * @version 1.0
 * @date 2021/12/12 16:23
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({GlobalExceptionConfig.class})
public @interface EnableCommonsGlobalExceptionHandler {
}
