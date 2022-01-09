package com.goudong.commons.annotation.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;


/**
 * 类描述：
 * 防止重复提交的方法，需要使用该注解进行拦截。
 * 默认值：2s不能重复提交,可以自定义
 * 注意：一般使用在 post 和 put 无幂等性请求方式
 *
 * @Author msi
 * @Date 2020/6/11 17:36
 * @Version 1.0
 */
@Target({ElementType.METHOD}) // 可以放在方法上
@Retention(RetentionPolicy.RUNTIME) //该注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
public @interface Repeat {

    /**
     * 重复提交的间隔设置为默认2s
     * @return
     */
    int time() default 2;

    /**
     * 重复提交的间隔时间单位，默认秒
     * @return
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
