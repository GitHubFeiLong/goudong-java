package com.goudong.commons.utils.redis;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.utils.AssertUtil;
import com.goudong.commons.utils.GenerateRedisKeyUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redis封装
 * @Author e-Feilong.Chen
 * @Date 2021/8/11 12:32
 */
@Slf4j
public class RedisOperationsUtil extends RedisTemplate implements RedisOperations{

    /**
     * 登录状态续期
     *
     * @param token
     * @param userId
     */
    @Override
    public void renewLoginStatus(String token, Long userId) {
        if(token != null) {
            String tokenMd5Key = JwtTokenUtil.generateRedisKey(token);
            // 获取完整key
            String key = GenerateRedisKeyUtil.generateByClever(RedisKeyEnum.OAUTH2_USER_INFO.getKey(), tokenMd5Key);
            if (super.hasKey(key)) {
                // redis 存的用户详细信息
                this.expire(key, RedisKeyEnum.OAUTH2_USER_INFO);
            }
            // 获取完整key
            String tokenInfoKey = GenerateRedisKeyUtil.generateByClever(RedisKeyEnum.OAUTH2_TOKEN_INFO.getKey(), userId);
            if (super.hasKey(tokenInfoKey)) {
                // redis 存的token信息
                this.expire(tokenInfoKey, RedisKeyEnum.OAUTH2_TOKEN_INFO);
            }
        }
    }


    /**
     * 登录统一处理redis
     * @param token
     * @param authorityUserDTO
     */
    public void login (String token, AuthorityUserDTO authorityUserDTO) {
        // 将用户信息保存到redis中
        // 将token进行md5加密作为redis key(16位16进制字符串)，然后保存用户详细信息
        String tokenMd5Key = JwtTokenUtil.generateRedisKey(token);

        // 只允许同一账号同时只能在线一个。
        // 获取上一次登录产生的token，删除redis中相关的 OAUTH2_USER_INFO信息。
        String lastLoginToken = this.getStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, authorityUserDTO.getId());
        if (lastLoginToken != null && !lastLoginToken.equals(token)) {
            String lastLoginTokenMd5Key = JwtTokenUtil.generateRedisKey(lastLoginToken);
            // 删除用户信息
            this.deleteKey(RedisKeyEnum.OAUTH2_USER_INFO, lastLoginTokenMd5Key);
        }

        // 存储redis || 追加时长
        this.setHashValue(RedisKeyEnum.OAUTH2_USER_INFO, BeanUtil.beanToMap(authorityUserDTO), tokenMd5Key);
        this.setStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, token, authorityUserDTO.getId());
    }

    /**
     * 退出统一处理redis
     * @param token
     * @param userId
     */
    public void logout(String token, Long userId) {
        // 将token进行md5加密作为redis key(16位16进制字符串)
        String tokenMd5Key = JwtTokenUtil.generateRedisKey(token);
        RedisKeyEnum[] keys = new RedisKeyEnum[]{RedisKeyEnum.OAUTH2_USER_INFO, RedisKeyEnum.OAUTH2_TOKEN_INFO};
        Object[][] params = new Object[][]{
                {tokenMd5Key},
                {userId},
        };
        // 删除redis中的数据
        this.deleteKeys(keys, params);
    }

    /**
     * 获取 dataType 为String的value
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public String getStringValue(RedisKeyEnum redisKeyEnum, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        return (String) super.opsForValue().get(key);
    }

    /**
     * 获取 dataType 为List的所有元素
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public <T> List<T> getListValue(RedisKeyEnum redisKeyEnum, Class<T> clazz, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        // 获取list（此时元素是LinkedHashMap）
        List range = super.opsForList().range(key, 0, -1);
        // 将集合中的元素转换成 clazz类型对象
        List<T> result = new ArrayList<>(range.size());
        range.forEach(p->{
            T t = BeanUtil.toBean(p, clazz);
            result.add(t);
        });
        
        return result;
    }

    /**
     * 获取 dataType 为Set的所有元素
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public Set getSetValue(RedisKeyEnum redisKeyEnum, Object... param) {
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
    public TreeSet getZSetValue(RedisKeyEnum redisKeyEnum, Object... param) {
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
    public <T> T getHashValue(RedisKeyEnum redisKeyEnum, Class<T> clazz, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        // 将Map转为Bean
        Map entries = super.opsForHash().entries(key);

        return BeanUtil.toBean(entries, clazz);
    }

    /**
     * 设置String类型
     *
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    @Override
    public void setStringValue(RedisKeyEnum redisKeyEnum, String value, Object... param) {
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
    public void setStringValueCustomizeExpire (RedisKeyEnum redisKeyEnum, String value, int time, TimeUnit timeUnit, Object... param) {
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
    public void setListValue(RedisKeyEnum redisKeyEnum, List value, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);

        // 删除key
        if (super.hasKey(key)) {
            super.delete(key);
        }
        // 当value为空集合时添加会报错,所以这里判断下
        if (value != null && !value.isEmpty()) {
            super.opsForList().leftPushAll(key, value);
            expire(key, redisKeyEnum);
        }

    }

    /**
     * 设置Set类型
     *
     * @param redisKeyEnum
     * @param value
     * @param param
     */
    @Override
    public void setSetValue(RedisKeyEnum redisKeyEnum, Set value, Object... param) {
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
    public void setZSetValue(RedisKeyEnum redisKeyEnum, TreeSet value, Object... param) {
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
    public void setHashValue(RedisKeyEnum redisKeyEnum, Map value, Object... param) {
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
    public void deleteKey(RedisKeyEnum redisKeyEnum, Object... param) {
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
    public void deleteKeys(RedisKeyEnum[] redisKeyEnums, Object[][] params) {
        AssertUtil.notEmpty(redisKeyEnums, "RedisKeyEnum数组不能为空");
        AssertUtil.notEmpty(params, "params数组不能为空");

        AssertUtil.isTrue(redisKeyEnums.length == params.length, "参数长度错误");
        if (redisKeyEnums.length != params.length) {
            log.error("deleteKeys redisKeyEnums.length:{}, params.length:{}", redisKeyEnums.length, params.length);
        }
        // 获取完整的 key
        for (int i = 0; i < redisKeyEnums.length; i++) {
            // 每个key的详细参数
            Object[] param = params[i];
            String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnums[i].getKey(), param);
            super.delete(key);
        }
        log.info("删除key成功");
    }

    /**
     * 检查key是否存在
     *
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public boolean hasKey(RedisKeyEnum redisKeyEnum, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        return super.hasKey(key);
    }

    /**
     * 当 redisKeyEnum 的time大于0时，需要设置过期时间
     * @param key
     * @param redisKeyEnum
     */
    public boolean expire(String key, RedisKeyEnum redisKeyEnum){
        if (redisKeyEnum.getTime() > 0) {
            boolean result = super.expire(key, redisKeyEnum.getTime(), redisKeyEnum.getTimeUnit());
            if (!result) {
                log.error("更新redis key过期时间错误（key：{} 时长：{} 时间单位：{}） ，该key可能不存在", key, redisKeyEnum.getTime(), redisKeyEnum.getTimeUnit());
            }
            return result;
        }
        return false;
    }

    /**
     * 获取key的失效时长
     * 返回值为-1时 此键值没有设置过期日期
     *
     * 返回值为-2时 不存在此键
     * @param redisKeyEnum
     * @param param
     * @return
     */
    @Override
    public long getExpire(RedisKeyEnum redisKeyEnum, Object... param) {
        //此方法返回单位为秒过期时长
        String key = GenerateRedisKeyUtil.generateByClever(redisKeyEnum.getKey(), param);
        return super.opsForValue().getOperations().getExpire(key);
    }
}
