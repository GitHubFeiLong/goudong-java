package com.goudong.commons.core.context;

import com.goudong.commons.dto.oauth2.BaseUserDTO;
import org.springframework.util.Assert;

/**
 * 类描述：
 * 获取当前请求的用户信息
 * 模仿 org.springframework.security.core.context.ThreadLocalSecurityContextHolderStrategy
 * @author msi
 * @version 1.0
 * @date 2022/1/23 10:59
 */
public final class UserContext {

    //~fields
    //==================================================================================================================
    private static final ThreadLocal<BaseUserDTO> threadLocal = new ThreadLocal<>();

    //~methods
    //==================================================================================================================
    private UserContext() {
    }

    /**
     * 设置当前请求的用户信息
     * @param baseUserDTO
     */
    public static void set(BaseUserDTO baseUserDTO) {
        Assert.notNull(baseUserDTO, "Only non-null BaseUserDTO instances are permitted");
        threadLocal.set(baseUserDTO);
    }

    /**
     * 获取当前请求的用户信息
     * @return
     */
    public static BaseUserDTO get() {
        return threadLocal.get();
    }

    /**
     * 处理完请求，清除内容
     */
    public static void remove() {
        threadLocal.remove();
    }
}