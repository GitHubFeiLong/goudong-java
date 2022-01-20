package com.goudong.user.enumerate;

import com.goudong.commons.frame.redis.RedisKeyProvider;
import com.goudong.commons.dto.oauth2.WhitelistDTO;
import org.springframework.data.redis.connection.DataType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 用户服务的redis key，及 过期时间，及时间的单位
 * 参数使用`${}`进行包裹，例如"user:${user-id}",RedisTool会动态设置参数值
 *
 * @author msi
 * @date 2022/1/15 17:46
 * @version 1.0
 */
public enum RedisKeyProviderEnum implements RedisKeyProvider {
    /**
     * 白名单
     */
    WHITELIST("goudong-user-server:whitelist", DataType.LIST, WhitelistDTO.class, 1, TimeUnit.DAYS),
    ;
    //~fields
    //==================================================================================================================

    /**
     * redis-key模板字符串，使用`${}`包裹需替换的字符串.
     */
    @NotBlank
    public String key;

    /**
     * 保存到redis中的数据类型,默认是String
     *
     * @see DataType
     */
    @NotNull
    public DataType redisType;

    /**
     * 使用RedisTool工具类，在获取key数据后，将其转为javaType
     * 当在redis中存储一个用户列表时,示例：
     * redisType=list
     * javaType=用户对象
     */
    @NotNull
    public Class javaType;

    /**
     * redis-key 过期时长, 默认-1,当值小于0时，表示不设置失效时间.
     */
    public long time = -1;

    /**
     * redis-key 过期时长单位
     */
    public TimeUnit timeUnit;

    //~construct methods
    //==================================================================================================================

    /**
     *
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     */
    RedisKeyProviderEnum(String key, DataType redisType, Class javaType){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
    }

    /**
     *
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     * @param time 过期时长
     * @param timeUnit 过期时长单位
     */
    RedisKeyProviderEnum(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
        this.time = time;
        this.timeUnit = timeUnit;
    }

    //~methods
    //==================================================================================================================
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public DataType getRedisType() {
        return redisType;
    }

    @Override
    public Class getJavaType() {
        return javaType;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
