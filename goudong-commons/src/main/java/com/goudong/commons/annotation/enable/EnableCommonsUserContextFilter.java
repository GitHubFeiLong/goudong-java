package com.goudong.commons.annotation.enable;

import com.goudong.commons.config.UserContextFilterConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中配置的 UserContextFilter 到过滤器链
 * 注意：如果集成了security，那么此种方式无效，需要手动添加到security的过滤器链中
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
