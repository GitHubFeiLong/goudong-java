package com.goudong.boot.web;

import com.goudong.boot.web.config.ExceptionHandlerConfiguration;
import com.goudong.boot.web.config.PropertiesConfig;
import com.goudong.boot.web.config.WebMvcConfig;
import com.goudong.boot.web.core.ErrorAttributesServiceImpl;
import com.goudong.boot.web.core.ErrorController;
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
@Import({PropertiesConfig.class, ExceptionHandlerConfiguration.class, WebMvcConfig.class, ErrorAttributesServiceImpl.class, ErrorController.class})
public @interface EnableCommonsWebMvcConfig {
}
