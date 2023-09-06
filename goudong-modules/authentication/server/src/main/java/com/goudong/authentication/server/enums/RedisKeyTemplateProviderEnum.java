package com.goudong.authentication.server.enums;

import com.goudong.authentication.server.rest.req.BaseAppDropDown;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDown;
import com.goudong.boot.redis.core.RedisKeyProvider;
import org.springframework.data.redis.connection.DataType;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 15:36
 */
public enum RedisKeyTemplateProviderEnum  implements RedisKeyProvider {
    // 分布式锁
    //==================================================================================================================

    // 缓存应用
    //==================================================================================================================
    /**
     * app
     */
    APP_ID("server:app:${appId}", DataType.STRING, String.class, 24, TimeUnit.HOURS),

    /**
     * app下拉
     */
    APP_DROP_DOWN("server:app:drop-down", DataType.LIST, BaseAppDropDown.class, 24, TimeUnit.HOURS),

    // 缓存用户
    //==================================================================================================================
    /**
     * 用户下拉
     */
    USER_DROP_DOWN("server:user:drop-down:${appId}", DataType.LIST, BaseUserDropDown.class, 24, TimeUnit.HOURS),


    // 缓存角色
    //==================================================================================================================
    ROLE_DROP_DOWN("server:role:drop-down:${appId}", DataType.LIST, BaseUserDropDown.class, 24, TimeUnit.HOURS),
    ;

    public String key;
    public DataType redisType;
    public Class javaType;
    public long time = -1L;
    public TimeUnit timeUnit;
    private RedisKeyTemplateProviderEnum(String key, DataType redisType, Class javaType) {
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
    }

    private RedisKeyTemplateProviderEnum(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit) {
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
        this.time = time;
        this.timeUnit = (TimeUnit) Optional.ofNullable(timeUnit).orElseGet(() -> {
            return TimeUnit.SECONDS;
        });
    }

    public String getKey() {
        return this.key;
    }

    public DataType getRedisType() {
        return this.redisType;
    }

    public Class getJavaType() {
        return this.javaType;
    }

    public long getTime() {
        return this.time;
    }

    public TimeUnit getTimeUnit() {
        return this.timeUnit;
    }
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
}
