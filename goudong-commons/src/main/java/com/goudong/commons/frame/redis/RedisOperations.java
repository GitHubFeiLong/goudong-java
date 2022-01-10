package com.goudong.commons.frame.redis;

import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.core.RedisKeyEnum;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * 操作redis的基本api
 * @author e-Feilong.Chen
 * @date 2021/8/11 10:24
 */
@Deprecated
public interface RedisOperations {

    /**
     * 登录状态续期
     * @param token
     * @param userId
     */
    void renewLoginStatus(String token, Long userId);

    /**
     * 登录统一处理redis
     * @param token
     * @param authorityUserDTO
     */
    void login (String token, AuthorityUserDTO authorityUserDTO);

    /**
     * 退出统一处理redis
     * @param token
     * @param userId
     */
    void logout(String token, Long userId);

    /**
     * 获取 dataType 为String的value
     * @param redisKeyEnum
     * @param param
     * @return
     */
    String getStringValue(RedisKeyEnum redisKeyEnum, Object... param);

    /**
     * 获取 dataType 为List的所有元素
     * @param redisKeyEnum
     * @param param
     * @return
     */
    <T> List<T> getListValue(RedisKeyEnum redisKeyEnum, Class<T> clazz, Object... param);
    /**
     * 获取 dataType 为Set的所有元素
     * @param redisKeyEnum
     * @param param
     * @return
     */
    Set getSetValue(RedisKeyEnum redisKeyEnum, Object... param);
    /**
     * 获取 dataType 为ZSet的所有元素
     * @param redisKeyEnum
     * @param param
     * @return
     */
    TreeSet getZSetValue(RedisKeyEnum redisKeyEnum, Object... param);
    /**
     * 获取 dataType 为Hash的所有元素
     * @param redisKeyEnum
     * @param param
     * @return
     */
    <T> T getHashValue(RedisKeyEnum redisKeyEnum, Class<T> clazz, Object... param);

    /**
     * 设置String类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setStringValue(RedisKeyEnum redisKeyEnum, String value, Object... param);

    /**
     * 设置String类型,不同的是，自定义时间和单位
     * @param redisKeyEnum redis key枚举
     * @param value 需要设置的值
     * @param time 过期时间
     * @param timeUnit 时间单位
     * @param param 替换模板字符串
     */
    void setStringValueCustomizeExpire (RedisKeyEnum redisKeyEnum, String value, int time, TimeUnit timeUnit, Object... param);

    /**
     * 设置List类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setListValue(RedisKeyEnum redisKeyEnum, List value, Object... param);

    /**
     * 设置Set类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setSetValue(RedisKeyEnum redisKeyEnum, Set value, Object... param);

    /**
     * 设置ZSet类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setZSetValue(RedisKeyEnum redisKeyEnum, TreeSet value, Object... param);

    /**
     * 设置Hash类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setHashValue(RedisKeyEnum redisKeyEnum, Map value, Object... param);


    /**
     * 删除单个key
     * @param redisKeyEnum
     * @param param
     */
    void deleteKey (RedisKeyEnum redisKeyEnum, Object ... param);

    /**
     * 删除多个key
     * 删除指定的key，注意长度必须一致！！！
     * @param redisKeyEnums
     * @param params
     */
    void deleteKeys (RedisKeyEnum[] redisKeyEnums, Object[][] params);

    /**
     * 检查key是否存在
     * @param redisKeyEnum
     * @param param
     * @return
     */
    boolean hasKey (RedisKeyEnum redisKeyEnum, Object... param);

    /**
     * 当 redisKeyEnum 的time大于0时，需要设置过期时间
     * @param key
     * @param redisKeyEnum
     */
    boolean expire(String key, RedisKeyEnum redisKeyEnum);

    /**
     * 获取key的失效时长
     * 返回值为-1时 此键值没有设置过期日期
     *
     * 返回值为-2时 不存在此键
     * @param redisKeyEnum
     * @param param
     * @return
     */
    long getExpire(RedisKeyEnum redisKeyEnum, Object... param);
}
