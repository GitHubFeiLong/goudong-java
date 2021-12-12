package com.goudong.commons.annotation.enable;

import com.goudong.commons.aop.RepeatAop;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 启用commons模块中配置的防止重复提交
 * @author msi
 * @version 1.0
 * @date 2021/12/11 17:56
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(RepeatAop.class)
public @interface EnableCommonsRepeatAop {
}
