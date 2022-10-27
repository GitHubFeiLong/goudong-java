package com.goudong.commons.annotation.enable;

import com.goudong.boot.exception.core.ErrorAttributes;
import com.goudong.boot.exception.core.ErrorController;
import com.goudong.commons.config.WebMvcConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中配置的 mvc配置
 * @author msi
 * @version 1.0
 * @date 2021/12/11 17:56
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({WebMvcConfig.class, ErrorAttributes.class, ErrorController.class})
public @interface EnableCommonsWebMvcConfig {
}
