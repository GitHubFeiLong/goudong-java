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
public final class GoudongContext {

    //~fields
    //==================================================================================================================
    private static final ThreadLocal<Context> threadLocal = new ThreadLocal<>();

    //~methods
    //==================================================================================================================
    private GoudongContext() {
    }

    /**
     * 设置当前请求的用户信息
     * @param context
     */
    public static void set(Context context) {
        AssertUtil.isNotNull(context, "Only non-null context instances are permitted");
        threadLocal.set(context);
    }

    /**
     * 获取当前请求的用户信息
     * @return
     */
    public static Context get() {
        return threadLocal.get();
    }

    /**
     * 处理完请求，清除内容
     */
    public static void remove() {
        threadLocal.remove();
    }
}
