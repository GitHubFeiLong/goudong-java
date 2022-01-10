package com.goudong.commons.frame.redis;

import lombok.Getter;
import org.springframework.data.redis.connection.DataType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redisKey的公共属性和行为定义，其它服务需要继承
 *
 * @Author e-Feilong.Chen
 * @Date 2022/1/10 13:34
 */
@Getter
public abstract class AbstractRedisKey {

    //~fields
    //==================================================================================================================

    /**
     * redis-key模板字符串，使用`{}`包裹需替换的字符串.
     */
    @NotBlank
    protected String key;

    /**
     * 保存到redis中的数据类型,默认是String
     *
     * @see DataType
     */
    @NotNull
    protected DataType redisType;

    /**
     * 使用RedisTool工具类，在获取key数据后，将其转为javaType
     * 当在redis中存储一个用户列表时,示例：
     * redisType=list
     * javaType=用户对象
     */
    @NotNull
    protected Class javaType;

    /**
     * redis-key 过期时长, 默认-1,当值小于0时，表示不设置失效时间.
     */
    protected long time = -1;

    /**
     * redis-key 过期时长单位
     */
    protected TimeUnit timeUnit;

    //~construct methods
    //==================================================================================================================

    /**
     *
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     */
    protected AbstractRedisKey(String key, DataType redisType, Class javaType){
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
    protected AbstractRedisKey(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
        this.time = time;
        this.timeUnit = timeUnit;
    }
}
