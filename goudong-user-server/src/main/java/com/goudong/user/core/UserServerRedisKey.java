package com.goudong.user.core;

import com.goudong.commons.dto.user.BaseWhitelist2RedisDTO;
import com.goudong.commons.frame.redis.AbstractRedisKey;
import com.goudong.user.po.BaseUserPO;
import org.springframework.data.redis.connection.DataType;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 用户服务RedisKey实现
 * @Author e-Feilong.Chen
 * @Date 2022/1/10 13:55
 */
public class UserServerRedisKey extends AbstractRedisKey {
    //~fields
    //==================================================================================================================
    /**
     * 白名单
     */
    public static final AbstractRedisKey WHITELIST = new UserServerRedisKey("goudong:goudong-user-server:whitelist", DataType.LIST, BaseWhitelist2RedisDTO.class);

    /**
     * 测试
     */
    public static final AbstractRedisKey STRING = new UserServerRedisKey("goudong:goudong-user-server:string", DataType.STRING, String.class);
    public static final AbstractRedisKey HASH = new UserServerRedisKey("goudong:goudong-user-server:hash", DataType.HASH, BaseUserPO.class);
    public static final AbstractRedisKey LIST = new UserServerRedisKey("goudong:goudong-user-server:list", DataType.LIST, String.class);
    public static final AbstractRedisKey SET = new UserServerRedisKey("goudong:goudong-user-server:set", DataType.SET, String.class);

    //~construct methods
    //==================================================================================================================
    /**
     * @param key       key模板字符串
     * @param redisType redis数据类型
     * @param javaType  java数据类型
     */
    public UserServerRedisKey(String key, DataType redisType, Class javaType) {
        super(key, redisType, javaType);
    }

    /**
     * @param key       key模板字符串
     * @param redisType redis数据类型
     * @param javaType  java数据类型
     * @param time      过期时长
     * @param timeUnit  过期时长单位
     */
    public UserServerRedisKey(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit) {
        super(key, redisType, javaType, time, timeUnit);
    }
}
