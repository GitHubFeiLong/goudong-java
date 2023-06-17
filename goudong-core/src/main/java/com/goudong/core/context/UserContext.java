package com.goudong.core.context;

import com.goudong.core.util.AssertUtil;

/**
 * 类描述：
 * 获取当前请求的用户信息
 * 模仿 org.springframework.security.core.context.ThreadLocalSecurityContextHolderStrategy
 * @author msi
 * @version 1.0
 * @date 2022/1/23 10:59
 */
@Deprecated
public final class UserContext {

    //~fields
    //==================================================================================================================
    private static final ThreadLocal<User> threadLocal = new ThreadLocal<>();

    //~methods
    //==================================================================================================================
    private UserContext() {
    }

    /**
     * 设置当前请求的用户信息
     * @param user
     */
    public static void set(User user) {
        AssertUtil.isNotNull(user, "Only non-null user instances are permitted");
        threadLocal.set(user);
    }

    /**
     * 获取当前请求的用户信息
     * @return
     */
    public static User get() {
        return threadLocal.get();
    }

    /**
     * 处理完请求，清除内容
     */
    public static void remove() {
        threadLocal.remove();
    }
}
