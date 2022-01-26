package com.goudong.commons.annotation.enable;

import com.goudong.commons.config.FeignConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons配置的Feign
 * @author msi
 * @version 1.0
 * @date 2021/12/11 17:56
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({FeignConfig.class})
public @interface EnableCommonsFeignConfig {
}
