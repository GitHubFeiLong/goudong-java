package com.goudong.commons.framework.redis;

import org.springframework.data.redis.connection.DataType;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * RedisKey定义接口，能获取到一些关键信息
 * @Author e-Feilong.Chen
 * @Date 2022/1/11 14:48
 */
public interface RedisKeyProvider extends Serializable {
    //~methods
    //==================================================================================================================

    /**
     * 获取redis-key模板字符串
     * @return
     */
    String getKey();

    /**
     * 获取Redis的数据类型
     * @return
     */
    DataType getRedisType();

    /**
     * 获取Java类型
     * @return
     */
    Class getJavaType();

    /**
     * 获取过期时长
     * @return
     */
    long getTime();

    /**
     * 获取过期时长单位
     * @return
     */
    TimeUnit getTimeUnit();
}
