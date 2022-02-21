package com.goudong.commons.annotation.enable;


import com.goudong.commons.config.JacksonConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中 Jackson配置
 * @auther msi
 * @date 2022/1/20 9:00
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({JacksonConfig.class})
public @interface EnableCommonsJacksonConfig {
}
