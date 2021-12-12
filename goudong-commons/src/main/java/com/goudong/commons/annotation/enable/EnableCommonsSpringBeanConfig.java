package com.goudong.commons.annotation.enable;

import com.goudong.commons.config.SpringBeanConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中配置的SpringBean操作类
 * @author msi
 * @version 1.0
 * @date 2021/12/11 17:56
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({SpringBeanConfig.class})
public @interface EnableCommonsSpringBeanConfig {
}
