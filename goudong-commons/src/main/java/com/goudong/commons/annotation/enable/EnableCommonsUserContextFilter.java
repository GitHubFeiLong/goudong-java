package com.goudong.commons.annotation.enable;

import com.goudong.commons.config.UserContextFilterConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中配置的 UserContextFilter 到过滤器链
 * @author cfl
 * @date 2022/8/1 0:36
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({UserContextFilterConfig.class})
public @interface EnableCommonsUserContextFilter {
}
