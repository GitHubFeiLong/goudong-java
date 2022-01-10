package com.goudong.user.core;

import com.goudong.commons.dto.user.BaseWhitelist2RedisDTO;
import com.goudong.commons.frame.redis.AbstractRedisKey;
import lombok.Getter;
import org.springframework.data.redis.connection.DataType;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 用户服务RedisKey实现
 * @Author e-Feilong.Chen
 * @Date 2022/1/10 13:55
 */
@Getter
public class UserServerRedisKey extends AbstractRedisKey {

    /**
     * 白名单
     */
    public static final AbstractRedisKey WHITELIST = new UserServerRedisKey("goudong:goudong-user-server:whitelist", DataType.LIST, -1, BaseWhitelist2RedisDTO.class);
    /**
     * 测试
     */
    public static final AbstractRedisKey TEST = new UserServerRedisKey("goudong:goudong-user-server:test", -1, DataType.STRING, Integer.class);

    public UserServerRedisKey() {
    }

    /**
     * 定义key模板
     *
     * @param key key模板
     */
    public UserServerRedisKey(String key) {
        super(key);
    }

    /**
     * 定义key模板和redis的数据类型
     *
     * @param key       key模板
     * @param redisType redis数据类型
     */
    public UserServerRedisKey(String key, DataType redisType) {
        super(key, redisType);
    }

    /**
     * 定义key模板和redis的数据类型
     *
     * @param key       key模板
     * @param redisType redis数据类型
     * @param time      过期时长
     */
    public UserServerRedisKey(String key, DataType redisType, int time) {
        super(key, redisType, time);
    }

    /**
     * 定义key模板和redis的数据类型
     *
     * @param key       key模板
     * @param redisType redis数据类型
     * @param time      过期时长
     * @param timeUnit  过期时长单位
     */
    public UserServerRedisKey(String key, DataType redisType, int time, TimeUnit timeUnit) {
        super(key, redisType, time, timeUnit);
    }

    /**
     * 定义key模板和redis的数据类型
     *
     * @param key       key模板
     * @param redisType redis数据类型
     * @param time      过期时长
     * @param timeUnit  过期时长单位
     * @param javaType  java数据类型
     */
    public UserServerRedisKey(String key, DataType redisType, int time, TimeUnit timeUnit, Class javaType) {
        super(key, redisType, time, timeUnit, javaType);
    }

    /**
     * 定义key模板和失效时间
     *
     * @param key  key模板
     * @param time 过期时长
     */
    public UserServerRedisKey(String key, int time) {
        super(key, time);
    }

    /**
     * 定义key模板和Redis的失效时间和精度
     *
     * @param key      key模板
     * @param time     过期时长
     * @param timeUnit 过期时长单位
     */
    public UserServerRedisKey(String key, int time, TimeUnit timeUnit) {
        super(key, time, timeUnit);
    }

    /**
     * 定义key模板和Redis的失效时间和精度
     *
     * @param key      key模板
     * @param time     过期时长
     * @param timeUnit 过期时长单位
     * @param javaType java数据类型
     */
    public UserServerRedisKey(String key, int time, TimeUnit timeUnit, Class javaType) {
        super(key, time, timeUnit, javaType);
    }

    /**
     * 定义key模板和redis的数据类型，以及java参数类型
     *
     * @param key      key模板
     * @param timeUnit 过期时长单位
     */
    public UserServerRedisKey(String key, TimeUnit timeUnit) {
        super(key, timeUnit);
    }

    /**
     * 定义key模板和Redis的失效时间和精度以及Redis的数据类型
     *
     * @param key      key模板
     * @param timeUnit 过期时长单位
     * @param javaType java数据类型
     */
    public UserServerRedisKey(String key, TimeUnit timeUnit, Class javaType) {
        super(key, timeUnit, javaType);
    }

    /**
     * 定义key模板和Redis的失效时间和精度以及Redis的数据类型，java数据类型
     *
     * @param key      key模板
     * @param javaType java数据类型
     */
    public UserServerRedisKey(String key, Class javaType) {
        super(key, javaType);
    }
}
