package com.goudong.commons.utils;


import com.alibaba.fastjson.JSON;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.enumerate.RedisKeyExpirationEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    @Autowired
    RedisTemplate redisTemplate;

    /**
     * 获取redis中的key对应的集合
     * @param redisKeyExpirationEnum reids key枚举
     * @param clazz 指定存储时的class 对象
     * @return
     */
    public <T> List<T> getListValue (RedisKeyExpirationEnum redisKeyExpirationEnum, Class clazz) {

        // 获取 redis中
        return (List<T>) Optional.ofNullable(getListByKey(redisKeyExpirationEnum.getKey(), clazz)).orElseGet(()->new ArrayList<>());
    }

    /**
     * 根据 redisKeyExpirationEnum 对象 和 param 参数 获取指定的redis keu， 然后获取redis中的数据。
     * @param redisKeyExpirationEnum    reids key枚举
     * @param clazz     指定存储时的class 对象
     * @param param     需要替换的参数
     * @return
     */
    public <T> List<T> getListValue (RedisKeyExpirationEnum redisKeyExpirationEnum, Class clazz, String... param) {
        String keyTemplate = redisKeyExpirationEnum.getKey();
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
     * 根据 redisKeyExpirationEnum 对象 保存redis数据
     * @param redisKeyExpirationEnum    reids key枚举
     * @param clazz
     * @param param    需要替换的参数
     * @return
     */
    public <T> T getValue (RedisKeyExpirationEnum redisKeyExpirationEnum, Class clazz, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyExpirationEnum.getKey(), param);

        // 判断有无值
        Object o = redisTemplate.opsForValue().get(key);
        if (Objects.nonNull(o)) {
            return (T) JSON.parseObject(o.toString(), clazz);
        }

        return null;
    }

    /**
     * 根据 redisKeyExpirationEnum 对象 保存redis数据
     * @param redisKeyExpirationEnum    redis key枚举
     * @param value     需要设置的值
     * @return
     */
    public void setValue (RedisKeyExpirationEnum redisKeyExpirationEnum, Object value) {
        // 获取完整的 key
        String key = redisKeyExpirationEnum.getKey();
        int time = redisKeyExpirationEnum.getTime();
        if (time < 0) {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
        } else {
            // 设置到 redis中
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value), redisKeyExpirationEnum.getTime(), redisKeyExpirationEnum.getTimeUnit());
        }

    }

    /**
     * 根据 redisKeyExpirationEnum 对象 保存redis数据
     * @param redisKeyExpirationEnum    redis key枚举
     * @param value     需要设置的值
     * @param param     需要替换的参数
     * @return
     */
    public void setValue (RedisKeyExpirationEnum redisKeyExpirationEnum, Object value, String... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyExpirationEnum.getKey(), param);
        int time = redisKeyExpirationEnum.getTime();
        if (time < 0) {
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value));
        } else {
            // 设置到 redis中
            redisTemplate.opsForValue().set(key, JSON.toJSONString(value), redisKeyExpirationEnum.getTime(), redisKeyExpirationEnum.getTimeUnit());
        }
    }

    /**
     * 删除指定的key
     * @param redisKeyExpirationEnum    redis key枚举
     * @param param     模板参数
     */
    public void deleteKey (RedisKeyExpirationEnum redisKeyExpirationEnum, String ... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyExpirationEnum.getKey(), param);
        redisTemplate.delete(key);
    }

    /**
     * 删除指定的key，注意长度必须一致！！！
     * @param redisKeyExpirationEnums    redis key枚举数组
     * @param params     模板参数二维数组
     */
    public void deleteKeys (@Valid @NotEmpty(message = "RedisKeyExpirationEnum数组不能为空") RedisKeyExpirationEnum[] redisKeyExpirationEnums
            , @Valid @NotEmpty(message = "params数组不能为空") String[][] params) {
        if (redisKeyExpirationEnums.length != params.length) {
            log.error("deleteKeys redisKeyExpirationEnums.length:{}, params.length:{}", redisKeyExpirationEnums.length, params.length);
            BasicException.ServerException.methodParamError("参数长度错误");
        }
        // 获取完整的 key
        for (int i = 0; i < redisKeyExpirationEnums.length; i++) {
            // 每个key的详细参数
            String[] param = params[i];
            String key = GenerateRedisKeyUtil.generateByClever(redisKeyExpirationEnums[i].getKey(), param);
            redisTemplate.delete(key);
        }
        log.info("删除key成功");
    }

    /**
     * 存在key，就自增 increment， 不存在，就设置value为1
     * @param redisKeyExpirationEnum    key对象
     * @param increment     增量
     * @param param         参数
     */
    public void increment(RedisKeyExpirationEnum redisKeyExpirationEnum, int increment, String... param) {
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyExpirationEnum.getKey(), param);
        // 存在key，就自增
        if (redisTemplate.hasKey(key)) {
            redisTemplate.opsForValue().increment(key, increment);
        } else {
            // 不存在，就设置值
            redisTemplate.opsForValue().set(key, 1, redisKeyExpirationEnum.getTime(), redisKeyExpirationEnum.getTimeUnit());
        }
    }

    /**
     * 判断redis中是否存在指定key的缓存
     * @param redisKeyExpirationEnum redis key枚举
     * @param param 枚举替换参数
     * @return
     */
    public boolean hasKey (RedisKeyExpirationEnum redisKeyExpirationEnum, String ... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyExpirationEnum.getKey(), param);
        return redisTemplate.hasKey(key);
    }
}
