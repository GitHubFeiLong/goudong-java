package com.goudong.commons.annotation.enable;

import com.goudong.commons.config.JdbcTemplateConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 开启commons下的JdbcTemplate配置
 * @author msi
 * @date 2022/1/8 17:10
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(JdbcTemplateConfig.class)
public @interface EnableCommonsJdbcTemplateConfig {
}
