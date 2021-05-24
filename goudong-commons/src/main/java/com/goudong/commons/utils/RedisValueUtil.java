package com.goudong.commons.utils;


import com.alibaba.fastjson.JSON;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 获取指定key的value（前提是value是JSON字符串）
 * @ClassName RedisValueUtil
 * @Author msi
 * @Date 2020/12/28 11:24
 * @Version 1.0
 */
@Slf4j
@Component
public class RedisValueUtil {

    @Resource
    RedisTemplate redisTemplate;

    /**
     * 根据 redisKeyEnum 对象 保存redis数据
     * @param redisKeyEnum reids key枚举
     * @param param 需要替换的参数
     * @return
     */
    public <T> T getValue (RedisKeyEnum redisKeyEnum, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        // 判断有无值
        Object o = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(o)) {
            return (T) o;
        }

        return null;
    }

    /**
     * 根据 redisKeyEnum 对象 保存redis数据
     * @param redisKeyEnum redis key枚举
     * @param value 需要设置的值
     * @param param 需要替换的参数
     * @return
     */
    public void setValue (RedisKeyEnum redisKeyEnum, Object value, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);;

        int time = redisKeyEnum.getTime();
        if (time < 0) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            // 设置到 redis中
            redisTemplate.opsForValue().set(key, value, redisKeyEnum.getTime(), redisKeyEnum.getTimeUnit());
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
    public void setValue (RedisKeyEnum redisKeyEnum, Object value, int time, TimeUnit timeUnit, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);;

        if (time < 0) {
            redisTemplate.opsForValue().set(key, value);
        } else {
            // 设置到 redis中
            redisTemplate.opsForValue().set(key, value, time, timeUnit);
        }
    }


    /**
     * 获取redis中的key对应的集合
     * @param redisKeyEnum reids key枚举
     * @param clazz 指定存储时的class 对象
     * @return
     */
    public <T> List<T> getListValue (RedisKeyEnum redisKeyEnum, Class clazz) {

        // 获取 redis中
        return (List<T>) Optional.ofNullable(getListByKey(redisKeyEnum.getKey(), clazz)).orElseGet(()->new ArrayList<>());
    }

    /**
     * 根据 redisKeyEnum 对象 和 param 参数 获取指定的redis keu， 然后获取redis中的数据。
     * @param redisKeyEnum    reids key枚举
     * @param clazz     指定存储时的class 对象
     * @param param     需要替换的参数
     * @return
     */
    public <T> List<T> getListValue (RedisKeyEnum redisKeyEnum, Class clazz, String... param) {
        String keyTemplate = redisKeyEnum.getKey();
        // 模板字符串中包含“$” 就必须传参数，没有就不用
        String key = keyTemplate;
        if (keyTemplate.indexOf("$") != -1) {
            // 获取完整的 key
            key = GenerateRedisKeyUtil.generateByClever(keyTemplate, param);
        }

        // 获取 redis中
        return (List<T>) Optional.ofNullable(getListByKey(key, clazz)).orElseGet(()->new ArrayList<>());
    }

    /**
     * 从redis中获取指定的key的value
     * @param key       redis key
     * @param clazz     返回的类型
     * @return  指定key存在值，就直接返回，没有值返回null
     */
    private <T> List<T> getListByKey (String key, Class clazz) {
        Object obj = this.redisTemplate.opsForValue().get(key);
        // redis没有，返回null
        if (Objects.isNull(obj)) {
            return null;
        }
        // redis中有，就直接使用
        return JSON.parseArray(obj.toString(), clazz);
    }



    /**
     * 删除指定的key
     * @param redisKeyEnum    redis key枚举
     * @param param     模板参数
     */
    public void deleteKey (RedisKeyEnum redisKeyEnum, String ... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        redisTemplate.delete(key);
    }

    /**
     * 删除指定的key，注意长度必须一致！！！
     * @param redisKeyEnums    redis key枚举数组
     * @param params     模板参数二维数组
     */
    public void deleteKeys (RedisKeyEnum[] redisKeyEnums, String[][] params) {

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
            redisTemplate.delete(key);
        }
        log.info("删除key成功");
    }

    /**
     * 存在key，就自增 increment， 不存在，就设置value为1
     * @param redisKeyEnum    key对象
     * @param increment     增量
     * @param param         参数
     */
    public void increment(RedisKeyEnum redisKeyEnum, int increment, String... param) {
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        // 存在key，就自增
        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().increment(key, increment);
        } else {
            // 不存在，就设置值
            redisTemplate.opsForValue().set(key, 1, redisKeyEnum.getTime(), redisKeyEnum.getTimeUnit());
        }
    }

    /**
     * 判断redis中是否存在指定key的缓存
     * @param redisKeyEnum redis key枚举
     * @param param 枚举替换参数
     * @return
     */
    public boolean hasKey (RedisKeyEnum redisKeyEnum, String ... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        return redisTemplate.hasKey(key);
    }
}
