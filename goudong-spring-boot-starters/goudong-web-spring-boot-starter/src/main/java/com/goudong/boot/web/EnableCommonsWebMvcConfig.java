package com.goudong.boot.web;

import com.goudong.boot.web.config.ExceptionHandlerConfiguration;
import com.goudong.boot.web.config.WebMvcConfig;
import com.goudong.boot.web.core.ErrorAttributes;
import com.goudong.boot.web.core.ErrorController;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用web配置
 * @author msi
 * @version 1.0
 * @date 2021/12/11 17:56
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({ExceptionHandlerConfiguration.class, WebMvcConfig.class, ErrorAttributes.class, ErrorController.class})
@ConfigurationPropertiesScan(basePackages = {"com.goudong.boot.web.properties"})
public @interface EnableCommonsWebMvcConfig {
}
