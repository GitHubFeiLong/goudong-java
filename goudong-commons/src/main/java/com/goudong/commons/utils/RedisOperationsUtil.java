package com.goudong.commons.utils;

import com.goudong.commons.enumerate.RedisKeyEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redis封装
 * @Author e-Feilong.Chen
 * @Date 2021/8/11 12:32
 */
@Slf4j
public class RedisOperationsUtil extends RedisTemplate implements RedisOperations{

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取 dataType 为String的value
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public String getStringValue(RedisKeyEnum redisKeyEnum, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * 获取 dataType 为List的所有元素
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public List getListValue(RedisKeyEnum redisKeyEnum, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        return super.opsForList().range(key, 0 , -1);
    }

    /**
     * 获取 dataType 为Set的所有元素
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public Set getSetValue(RedisKeyEnum redisKeyEnum, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        return super.opsForSet().members(key);
    }

    /**
     * 获取 dataType 为ZSet的所有元素
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public TreeSet getZSetValue(RedisKeyEnum redisKeyEnum, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        return (TreeSet)super.opsForZSet().range(key, 0, -1);
    }

    /**
     * 获取 dataType 为Hash的所有元素
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public Map getHashValue(RedisKeyEnum redisKeyEnum, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        return super.opsForHash().entries(key);
    }

    /**
     * 设置String类型
     *
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    @Override
    public void setStringValue(RedisKeyEnum redisKeyEnum, String value, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        int time = redisKeyEnum.getTime();
        if (time < 0) {
            super.opsForValue().set(key, value);
        } else {
            // 设置到 redis中
            super.opsForValue().set(key, value, redisKeyEnum.getTime(), redisKeyEnum.getTimeUnit());
        }
    }
    /**
     * 根据 redisKeyEnum 对象 保存redis数据,不同的是，自定义时间和单位
     * @param redisKeyEnum redis key枚举
     * @param value 需要设置的值
     * @param time 过期时间
     * @param timeUnit 时间单位
     * @param param 替换模板字符串
     */
    @Override
    public void setStringValue (RedisKeyEnum redisKeyEnum, String value, int time, TimeUnit timeUnit, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);;
        if (time < 0) {
            super.opsForValue().set(key, value);
        } else {
            // 设置到 redis中
            super.opsForValue().set(key, value, time, timeUnit);
        }
    }

    /**
     * 设置List类型
     *
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    @Override
    public void setListValue(RedisKeyEnum redisKeyEnum, List value, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        // 删除key
        if (super.hasKey(key)) {
            super.delete(key);
        }
        super.opsForList().leftPushAll(key, value);
        expire(key, redisKeyEnum);
    }

    /**
     * 设置Set类型
     *
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    @Override
    public void setSetValue(RedisKeyEnum redisKeyEnum, Set value, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        super.opsForSet().add(key, value);
        expire(key, redisKeyEnum);
    }

    /**
     * 设置ZSet类型
     *
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    @Override
    public void setZSetValue(RedisKeyEnum redisKeyEnum, TreeSet value, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        super.opsForZSet().add(key, value);
        expire(key, redisKeyEnum);
    }

    /**
     * 设置Hash类型
     *
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    @Override
    public void setHashValue(RedisKeyEnum redisKeyEnum, Map value, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        super.opsForHash().putAll(key, value);
        expire(key, redisKeyEnum);
    }

    /**
     * 删除单个key
     *
     * @param redisKeyEnum
     * @param param
     */
    @Override
    public void deleteKey(RedisKeyEnum redisKeyEnum, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        super.delete(key);
    }

    /**
     * 删除多个key
     * 删除指定的key，注意长度必须一致！！！
     * @param redisKeyEnums
     * @param params
     */
    @Override
    public void deleteKeys(RedisKeyEnum[] redisKeyEnums, String[][] params) {
        AssertUtil.notEmpty(redisKeyEnums, "RedisKeyEnum数组不能为空");
        AssertUtil.notEmpty(params, "params数组不能为空");

        AssertUtil.isTrue(redisKeyEnums.length == params.length, "参数长度错误");
        if (redisKeyEnums.length != params.length) {
            log.error("deleteKeys redisKeyEnums.length:{}, params.length:{}", redisKeyEnums.length, params.length);
        }
        // 获取完整的 key
        for (int i = 0; i < redisKeyEnums.length; i++) {
            // 每个key的详细参数
            String[] param = params[i];
            String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnums[i].getKey(), param);
            super.delete(key);
        }
        log.info("删除key成功");
    }

    /**
     * 当 redisKeyEnum 的time大于0时，需要设置过期时间
     * @param key
     * @param redisKeyEnum
     */
    private void expire(String key, RedisKeyEnum redisKeyEnum){
        if (redisKeyEnum.getTime() > 0) {
            super.expire(key, redisKeyEnum.getTime(), redisKeyEnum.getTimeUnit());
        }
    }
}
