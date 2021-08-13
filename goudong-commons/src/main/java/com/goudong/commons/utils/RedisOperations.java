package com.goudong.commons.utils;

import com.goudong.commons.enumerate.RedisKeyEnum;

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
public interface RedisOperations {

    /**
     * 获取 dataType 为String的value
     * @param redisKeyEnum
     * @param param
     * @return
     */
    String getStringValue(RedisKeyEnum redisKeyEnum, String... param);

    /**
     * 获取 dataType 为List的所有元素
     * @param redisKeyEnum
     * @param param
     * @return
     */
    List getListValue(RedisKeyEnum redisKeyEnum, String... param);
    /**
     * 获取 dataType 为Set的所有元素
     * @param redisKeyEnum
     * @param param
     * @return
     */
    Set getSetValue(RedisKeyEnum redisKeyEnum, String... param);
    /**
     * 获取 dataType 为ZSet的所有元素
     * @param redisKeyEnum
     * @param param
     * @return
     */
    TreeSet getZSetValue(RedisKeyEnum redisKeyEnum, String... param);
    /**
     * 获取 dataType 为Hash的所有元素
     * @param redisKeyEnum
     * @param param
     * @return
     */
    Map getHashValue(RedisKeyEnum redisKeyEnum, String... param);

    /**
     * 设置String类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setStringValue(RedisKeyEnum redisKeyEnum, String value, String... param);

    /**
     * 设置String类型,不同的是，自定义时间和单位
     * @param redisKeyEnum redis key枚举
     * @param value 需要设置的值
     * @param time 过期时间
     * @param timeUnit 时间单位
     * @param param 替换模板字符串
     */
    void setStringValue (RedisKeyEnum redisKeyEnum, String value, int time, TimeUnit timeUnit, String... param);

    /**
     * 设置List类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setListValue(RedisKeyEnum redisKeyEnum, List value, String... param);

    /**
     * 设置Set类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setSetValue(RedisKeyEnum redisKeyEnum, Set value, String... param);

    /**
     * 设置ZSet类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setZSetValue(RedisKeyEnum redisKeyEnum, TreeSet value, String... param);

    /**
     * 设置Hash类型
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    void setHashValue(RedisKeyEnum redisKeyEnum, Map value, String... param);

    /**
     * 删除单个key
     * @param redisKeyEnum
     * @param param
     */
    void deleteKey (RedisKeyEnum redisKeyEnum, String ... param);

    /**
     * 删除多个key
     * 删除指定的key，注意长度必须一致！！！
     * @param redisKeyEnums
     * @param params
     */
    void deleteKeys (RedisKeyEnum[] redisKeyEnums, String[][] params);

    /**
     * 检查key是否存在
     * @param redisKeyEnum
     * @param param
     * @return
     */
    boolean hasKey (RedisKeyEnum redisKeyEnum, String... param);
}
