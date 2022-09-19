package com.goudong.commons.annotation.core;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;


/**
 * 类描述：
 * 放在控制层接口方法上，表明这个资源被加入菜单
 * @author cfl
 * @date 2022/9/19 21:22
 * @version 1.0
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HideMenu {

    /**
     * 白名单的备注描述
     * @return
     */
    @AliasFor("remark")
    String value() default "预置隐藏菜单资源";

    /**
     * 是否是api接口
     * @return true 是api接口，false 不是
     */
    boolean api() default true;

    /**
     * 是否是系统资源
     * @return true 是，false 不是
     */
    boolean sys() default true;

    /**
     * 备注
     * @return
     */
    @AliasFor("value")
    String remark() default "预置隐藏菜单资源";
}
