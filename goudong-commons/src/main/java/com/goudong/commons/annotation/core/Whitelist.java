package com.goudong.commons.annotation.core;

import java.lang.annotation.*;


/**
 * 注解描述：
 * 放在控制层接口方法上，表明这个资源被加入白名单
 * @author msi
 * @date 2022/1/8 16:17
 * @version 1.0
 */
//@Inherited
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Whitelist {

    /**
     * 白名单的备注描述
     * @return
     */
    String value() default "白名单";

    /**
     * 是否关闭该白名单
     * @return true关闭，false开启
     */
    boolean disable() default false;
}
