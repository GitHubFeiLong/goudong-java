package com.goudong.boot.redis;

import com.goudong.boot.redis.config.RedisConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中配置的redis配置
 * @author msi
 * @version 1.0
 * @date 2021/12/11 17:56
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RedisConfig.class)
public @interface EnableCommonsRedisConfig {
}
