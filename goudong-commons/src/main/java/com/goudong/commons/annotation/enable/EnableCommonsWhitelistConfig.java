package com.goudong.commons.annotation.enable;

import com.goudong.commons.config.WhitelistConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 开启commons下的白名单配置
 * @author msi
 * @date 2022/1/8 17:10
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(WhitelistConfig.class)
public @interface EnableCommonsWhitelistConfig {
}
