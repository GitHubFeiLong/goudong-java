package com.goudong.commons.annotation.core;

import java.lang.annotation.*;

/**
 * 注解描述：
 * 该注解用来管理服务间调用接口，关闭鉴权的注解。只需要在接口或控制器上加上该注解，那么对应的接口都可以在其他服务使用Feign的方式直接调用，不需要在关心权限问题。
 * 注意：服务之间使用Feign调用，不会走网关，如果在网关鉴权，那么这个内部接口没有任何意义，如果后面使用全局拦截器，每个服务每个接口都鉴权那么就可以正常使用它
 * @author cfl
 * @date 2022/8/1 13:17
 * @version 1.0
 */
@Documented
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME) //该注解不仅被保存到class文件中，jvm加载class文件之后，仍然存在；
@Whitelist
public @interface Inner {

    /**
     * 接口备注描述
     * @return
     */
    String value() default "内部接口";

    /**
     * 是否关闭该白名单
     * @return true关闭，false开启
     */
    boolean disable() default false;
}
