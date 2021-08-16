package com.goudong.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;


/**
 * 类描述：
 * 放在控制层接口方法上，表明这个资源被加入白名单
 * @Author msi
 * @Date 2021/08/13 23:37:00
 * @Version 1.0
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) //该注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
public @interface IgnoreResource {
    /**
     * 白名单的备注描述
     * @return
     */
    String value() default "自定义api接口";
}
