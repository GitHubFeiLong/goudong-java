package com.goudong.boot.redis.core;

import com.sun.istack.internal.NotNull;
import org.springframework.data.redis.connection.DataType;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redis 的key，及 过期时间，及时间的单位, 其它服务直接复制使用。
 * 参数使用`${}`进行包裹，例如"user:${user-id}",RedisTool会动态设置参数值
 *
 * 注意：根据该模板创建构造方法需要保证一致性，不然有些其它功能使用时校验错误
 *
 * TODO 后期考虑使用内部类方式，继承一个父类，达到多继承的关系
 * @Author msi
 * @Date 2021/1/7 11:21
 * @Version 1.0
 */
public enum RedisKeyTemplateProviderEnum implements RedisKeyProvider{
    DELETE_KEY("test:delete-key:string", DataType.STRING, String.class),
    DELETE_KEYS_1("test:delete-keys:string1", DataType.STRING, String.class),
    DELETE_KEYS_2("test:delete-keys:string2", DataType.STRING, String.class),
    HAS_KEY("test:has-key:string2", DataType.STRING, String.class),
    REFRESH_KEY("test:refresh-key:string2", DataType.STRING, String.class, 10, TimeUnit.SECONDS),
    EXPIRE_KEY("test:expire-key:string1", DataType.STRING, String.class, 10, TimeUnit.SECONDS),
    ;
    //~fields
    //==================================================================================================================

    /**
     * redis-key模板字符串，使用`${}`包裹需替换的字符串.
     * @NotBlank
     */
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
     * 必须拥有该构造方法
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     */
    RedisKeyTemplateProviderEnum(String key, DataType redisType, Class javaType){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
    }

    /**
     * 必须拥有该构造方法
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     * @param time 过期时长
     * @param timeUnit 过期时长单位。 如果传入null，就使用默认秒作为单位
     */
    RedisKeyTemplateProviderEnum(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
        this.time = time;
        this.timeUnit = Optional.ofNullable(timeUnit).orElseGet(()->TimeUnit.SECONDS);
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
