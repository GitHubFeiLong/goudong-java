package com.goudong.commons.annotation.enable;

import com.goudong.commons.autoconfigure.HideMenuAutoConfiguration;
import com.goudong.commons.config.WebMvcConfig;
import com.goudong.commons.framework.mvc.error.ErrorAttributes;
import com.goudong.commons.framework.mvc.error.ErrorController;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 类描述：
 * 启用commons模块中配置的 启动服务保存隐藏菜单
 * @author cfl
 * @date 2022/9/19 22:37
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({HideMenuAutoConfiguration.class})
public @interface EnableCommonsHideMenuConfig {
}
