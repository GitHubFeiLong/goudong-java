package com.goudong.commons.annotation.enable;

import com.goudong.commons.config.SpringBeanToolConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中配置的SpringBeanTool
 * @auther msi
 * @date 2022/3/14 14:52
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(SpringBeanToolConfig.class)
public @interface EnableCommonsSpringBeanToolConfig {
}
