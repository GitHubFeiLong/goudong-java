package com.goudong.commons.annotation.aop;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;


/**
 * 类描述：
 * 防止重复提交的方法，需要使用该注解进行拦截, 使用AOP+分布式锁完成。
 * TODO 后期给该注解加上额外功能，比如指定接口限流（硬编码）：每个人对指定接口限定时间内，允许访问多少次等
 * 注意：一般使用在 post 和 put 无幂等性请求方式
 *
 * 如果注解使用@AliasFor，需要使用Spring的工具过去值
 * AnnotationUtils.getAnnotation(targetMethod, ApiRepeat.class);
 * @Author msi
 * @Date 2020/6/11 17:36
 * @Version 1.0
 */
@Documented
@Target({ElementType.METHOD}) // 可以放在方法上
@Retention(RetentionPolicy.RUNTIME) //该注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
public @interface ApiRepeat {

    @AliasFor(attribute = "time")
    @Deprecated
    int value() default 2;
    /**
     * 重复提交的间隔设置为默认2s
     * @return
     */
    @AliasFor(attribute = "value")
    @Deprecated
    int time() default 2;

    /**
     * 重复提交的间隔时间单位，默认秒
     * @return
     */
    @Deprecated
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 执行完方法后，是否删除缓存
     * true:删除，方法允许最大qps
     * false：指定时间过后才能进行访问。
     * @return
     */
    @Deprecated
    boolean deleteKey() default false;

}
