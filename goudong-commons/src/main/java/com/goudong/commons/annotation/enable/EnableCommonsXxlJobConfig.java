package com.goudong.commons.annotation.enable;

import com.goudong.commons.autoconfigure.XxlJobAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中配置的 xxl-job配置
 * @author cfl
 * @version 1.0
 * @date 2022/7/13 21:24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({XxlJobAutoConfiguration.class})
public @interface EnableCommonsXxlJobConfig {
}
