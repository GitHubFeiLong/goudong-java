package com.goudong.commons.frame.redis;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.enumerate.ServerExceptionEnum;
import com.goudong.commons.exception.redis.RedisToolException;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.commons.utils.core.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
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
    public boolean expire(AbstractRedisKey redisKey, long time, @NotNull TimeUnit timeUnit, Object... param) {

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
     * 类似门面,设置数据到redis,根据{@link AbstractRedisKey#redisType}和{@link AbstractRedisKey#javaType}进行设置值.
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link AbstractRedisKey#key}的模板参数
     * @return
     */
    public boolean set(@Valid AbstractRedisKey redisKey, Object value, Object... param){
        DataType dataType = redisKey.getRedisType();
        // TODO Class先不做校验，看下注解是否有效
        switch (dataType) {
            case STRING:
                return setString(redisKey, value, param);
            case HASH:
                return setHash(redisKey, value, param);
            case LIST:
                return setList(redisKey, value, param);
            case SET:
                return setSet(redisKey, value, param);
            default:
                String serverMessage = String.format("暂不支持redis设置【%s】类型的数据", dataType);
                throw new RedisToolException(ServerExceptionEnum.SERVER_ERROR, serverMessage);
        }

    }

    /**
     * 设置String类型数据到redis中
     * @see DataType#STRING
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link AbstractRedisKey#key}的模板参数
     * @return
     */
    private boolean setString(AbstractRedisKey redisKey, Object value, Object... param) {
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        if (redisKey.getTime() > 0) {
            super.opsForValue().set(key, value, redisKey.getTime(), redisKey.getTimeUnit());
        } else {
            super.opsForValue().set(key, value);
        }
        return true;

        // throw new RedisToolException(ServerExceptionEnum.SERVER_ERROR,
        //         String.format("设置String到redis中失败!redisType:%s,javaType:%s,value:%s",
        //                 redisKey.getRedisType(),
        //                 redisKey.getJavaType(),
        //                 value)
        // );
    }

    /**
     * 设置Hash类型数据到redis中.
     * @see DataType#HASH
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link AbstractRedisKey#key}的模板参数
     * @return
     */
    private boolean setHash(AbstractRedisKey redisKey, Object value, Object... param){
        /*
            参数校验
         */
        if (redisKey.getJavaType().isInstance(value)) {
            // 获取完整的 key
            String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
            // TODO 这里需要使用Lua脚本进行修改
            super.opsForHash().putAll(key, BeanUtil.beanToMap(value));
            if (redisKey.getTime() > 0) {
                // 设置过期时常
                return super.expire(key, redisKey.getTime(), redisKey.timeUnit);
            }

            return true;
        }

        throw new RedisToolException(ServerExceptionEnum.SERVER_ERROR,
                String.format("设置Hash到redis中失败!redisType:%s,javaType:%s,value:%s",
                        redisKey.getRedisType(),
                        redisKey.getJavaType(),
                        value)
        );
    }

    /**
     * 设置List类型数据到redis中.
     * @see DataType#LIST
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link AbstractRedisKey#key}的模板参数
     * @return
     */
    private boolean setList(AbstractRedisKey redisKey, Object value, Object[] param) {
        /*
            参数校验
        */
        List list = (List)value;
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        // 当value为空集合时添加会报错,所以这里判断下
        if (CollectionUtils.isEmpty(list)) {
            // 删除key
            if (super.hasKey(key)) {
                super.delete(key);
            }

            super.opsForList().leftPushAll(key, list);
            if (redisKey.getTime() > 0) {
                return super.expire(key, redisKey.getTime(), redisKey.timeUnit);
            }

            return true;
        }
        // 类型比较
        if (redisKey.getJavaType().isInstance(list.get(0))) {
            // 删除key
            if (super.hasKey(key)) {
                super.delete(key);
            }

            super.opsForList().leftPushAll(key, list);
            if (redisKey.getTime() > 0) {
                return super.expire(key, redisKey.getTime(), redisKey.timeUnit);
            }
            return true;
        }

        throw new RedisToolException(ServerExceptionEnum.SERVER_ERROR,
                String.format("设置List到redis中失败!redisType:%s,javaType:%s,value:%s",
                        redisKey.getRedisType(),
                        redisKey.getJavaType(),
                        value)
        );
    }

    /**
     * 设置List类型数据到redis中.
     * @see DataType#SET
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link AbstractRedisKey#key}的模板参数
     * @return
     */
    private boolean setSet(AbstractRedisKey redisKey, Object value, Object[] param) {
        Set set = (Set)value;
        if (CollectionUtils.isEmpty(set)) {

        }
        Object obj = set.stream().findFirst().get();
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        // 类型比较
        if (redisKey.getJavaType().isInstance(obj)) {
            super.opsForSet().add(key, set);
            return super.expire(key, redisKey.getTime(), redisKey.timeUnit);
        }

        throw new RedisToolException(ServerExceptionEnum.SERVER_ERROR,
                String.format("设置List到redis中失败!redisType:%s,javaType:%s,value:%s",
                        redisKey.getRedisType(),
                        redisKey.getJavaType(),
                        value)
        );

    }

}
