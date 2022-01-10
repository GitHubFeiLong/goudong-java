package com.goudong.commons.frame.redis;

import com.goudong.commons.enumerate.ServerExceptionEnum;
import com.goudong.commons.exception.redis.RedisToolException;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.commons.utils.core.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/1/10 15:16
 */
@Slf4j
@Validated
public class RedisTool extends RedisTemplate {

    //~methods
    //==================================================================================================================

    /**
     * 删除单个key
     *
     * @param redisKey
     * @param param
     */
    public void deleteKey(AbstractRedisKey redisKey, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        super.delete(key);
        LogUtil.debug(log, "redis-key:{}已被删除", key);
    }

    /**
     * 删除多个key
     * 删除指定的key，注意长度必须一致！！！
     *
     * @param redisKeys
     * @param params
     */
    public void deleteKeys(List<AbstractRedisKey> redisKeys, Object[][] params) {
        AssertUtil.notEmpty(redisKeys, "删除key时,redisKeys不能为空");
        AssertUtil.notEmpty(params, "删除key时,params数组不能为空");

        AssertUtil.isTrue(redisKeys.size() == params.length,
                String.format("删除redis-key时,参数长度不一致:redisKeys.size:%s,params.length:%s",
                        redisKeys.size(),
                        params.length)
        );

        // 循环删除key
        for (int i = 0; i < redisKeys.size(); i++) {
            this.deleteKey(redisKeys.get(i), params[i]);
        }
    }

    /**
     * 检查key是否存在
     *
     * @param redisKey
     * @param param
     * @return
     */
    public boolean hasKey(AbstractRedisKey redisKey, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        boolean hasKey = super.hasKey(key);
        LogUtil.debug(log, "redis-key:{}{}", key, hasKey ? "存在" : "不存在");
        return hasKey;
    }

    /**
     * 刷新指定key的过期时长
     *
     * @param redisKey
     * @param param
     */
    public boolean refresh(AbstractRedisKey redisKey, Object... param) {
        return this.expire(redisKey, redisKey.getTime(), redisKey.getTimeUnit(), param);
    }

    /**
     * 设置key指定过期时长
     *
     * @param redisKey
     * @param time
     * @param timeUnit
     * @param param
     */
    public boolean expire(AbstractRedisKey redisKey, int time, @NotNull TimeUnit timeUnit, Object... param) {

        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);

        boolean result = super.expire(key, time, timeUnit);

        if (!result) {
            LogUtil.error(log, "更新redis key过期时间错误（key：{} 时长：{} 时间单位：{}） ，该key可能不存在",
                    key,
                    time,
                    timeUnit);

            return false;
        }

        LogUtil.debug(log, "刷新redis-key:{}过期时间成功, ttl:{}s", key, timeUnit.toSeconds(time));
        return true;
    }

    /**
     * 获取key的失效时长
     * 返回值为-1时 此键值没有设置过期日期
     * <p>
     * 返回值为-2时 不存在此键
     *
     * @param redisKey
     * @param param
     * @return
     */
    public long getExpire(AbstractRedisKey redisKey, Object... param) {
        //此方法返回单位为秒过期时长
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        long ttl = super.opsForValue().getOperations().getExpire(key);
        if (ttl >= 0) {
            LogUtil.debug(log,"redis-key:{} ttl:{}", key, ttl);
        } else if (ttl == -1) {
            LogUtil.debug(log,"redis-key:{} ttl:{},该key未设置过期时长", key, ttl);
        } else if (ttl == -2) {
            LogUtil.warn(log,"redis-key:{} ttl:{},该key不存在", key, ttl);
        }
        return ttl;
    }

    /**
     * 设置数据到redis
     * @param redisKey
     * @param value
     * @param param
     * @return
     */
    public boolean set(@Valid AbstractRedisKey redisKey, Object value, Object... param){
        /*
            参数校验
         */
        DataType dataType = Optional.ofNullable(redisKey.getDataType())
                .orElseThrow(()->new RedisToolException(ServerExceptionEnum.SERVER_ERROR, "AbstractRedisKey.dataType不能为null"));
        // TODO Class先不做校验，看下注解是否有效

        Class clazz = redisKey.getClazz();

        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        int time = redisKey.getTime();

        switch (dataType) {
            case STRING:
                setString(redisKey, value, param);
                break;
            default:
                LogUtil.error(log,"暂不支持redis设置【{}】类型的数据", dataType.code());
                return false;
        }

        return true;

    }

    private void setString(AbstractRedisKey redisKey, Object value, Object... param) {
        /*
            参数校验
         */
        if (redisKey.getClazz().isInstance(value)) {
            String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
            if (redisKey.getTime() < 0) {
                super.opsForValue().set(key, value);
            } else {
                // 设置到 redis中
                super.opsForValue().set(key, value, redisKey.getTime(), redisKey.getTimeUnit());
            }
        }

        LogUtil.error(log, "redis-key");
    }
}
