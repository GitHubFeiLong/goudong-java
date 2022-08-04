package com.goudong.commons.framework.redis;

import org.springframework.data.redis.connection.DataType;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * RedisKey定义接口，能获取到一些关键信息
 *
 * 使用方式有两种：
 * 1. 使用枚举方式，参考 {@link RedisKeyTemplateProviderEnum}
 * 2. 使用常量方式，参考 {@link SimpleRedisKey}
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

    /**
     * 根据对象的getTime()和getTimeUnit() 获取一个second
     * 在设置key过期时间时，直接使用秒做为单位，方便加上随机数
     * @return
     */
    default long getTime2Second(RedisTool.Entry entry) {

        long time = getTime();

        // 如果gai key 设置的是永不失效，就不额外处理
        if (time == -1) {
            return time;
        }

        // 如果 开启了雪崩处理,加上指定秒
        if (entry != null && entry.getEnableSnowSlideHandlerSnow()) {
            return getTimeUnit().toSeconds(time) + entry.getSnowSlideHandlerSecond();
        }

        return getTimeUnit().toSeconds(time);
    }
}
