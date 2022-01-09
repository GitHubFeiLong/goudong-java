package com.goudong.commons.annotation.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 注解描述：
 * 放在控制层接口方法上，表明这个资源被加入白名单
 * @author msi
 * @date 2022/1/8 16:17
 * @version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Whitelist {

    /**
     * 白名单的备注描述
     * @return
     */
    String value() default "白名单";
}
