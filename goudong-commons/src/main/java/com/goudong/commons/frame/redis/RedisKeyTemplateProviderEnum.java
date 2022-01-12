package com.goudong.commons.frame.redis;

import com.goudong.commons.po.core.BasePO;
import org.springframework.data.redis.connection.DataType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redis 的key，及 过期时间，及时间的单位, 其它服务直接复制使用。
 * 下面的成员是测试需要
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
    GET_EXPIRE1("test:get-expire:string1:${id}", DataType.HASH, BasePO.class, 2, TimeUnit.HOURS),
    GET_EXPIRE2("test:get-expire:string2:${id}", DataType.HASH, BasePO.class, 2, TimeUnit.HOURS),
    GET_EXPIRE3("test:get-expire:string3:${id}", DataType.HASH, BasePO.class),
    // GET_EXPIRE("test:get-expire:string:{id}", DataType.HASH, BasePO.class, 2, TimeUnit.HOURS),
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
    RedisKeyTemplateProviderEnum(String key, DataType redisType, Class javaType){
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
    RedisKeyTemplateProviderEnum(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit){
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
