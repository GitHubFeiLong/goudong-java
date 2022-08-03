package com.goudong.commons.framework.redis;

import lombok.experimental.SuperBuilder;
import org.springframework.data.redis.connection.DataType;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 使用类来处理生成key，暂时还不实用，先留着后面优化
 * @Author e-Feilong.Chen
 * @Date 2022/1/11 12:30
 */
@SuperBuilder
public class SimpleRedisKey extends AbstractRedisKey{
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================
    /**
     * @param key       key模板字符串
     * @param redisType redis数据类型
     * @param javaType  java数据类型
     */
    public SimpleRedisKey(String key, DataType redisType, Class javaType) {
        super(key, redisType, javaType);
    }

    /**
     * @param key       key模板字符串
     * @param redisType redis数据类型
     * @param javaType  java数据类型
     * @param time      过期时长
     * @param timeUnit  过期时长单位
     */
    public SimpleRedisKey(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit) {
        super(key, redisType, javaType, time, timeUnit);
    }

    //~methods
    //==================================================================================================================
}
