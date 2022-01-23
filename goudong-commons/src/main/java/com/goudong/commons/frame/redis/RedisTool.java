package com.goudong.commons.frame.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.goudong.commons.enumerate.core.ServerExceptionEnum;
import com.goudong.commons.exception.redis.RedisToolException;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.commons.utils.core.PrimitiveTypeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * Redis操作RedisKeyProvider
 * TODO 这里需要使用Lua脚本进行修改，避免执行一半成功一半失败
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
     * @param redisKey redisKey对象
     * @param param 替换模板的参数
     * @return 删除成功时返回true,当删除的key不存在时,返回false.
     */
    public boolean deleteKey(RedisKeyProvider redisKey, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        // String key = StringUtil.format(redisKey.getKey(), param);
        // key 不存在时，删除失败，返回false
        boolean delete = super.delete(key);
        if (delete) {
            LogUtil.debug(log, "redis-key:{}已被删除", key);
        } else {
            LogUtil.warn(log, "redis-key:{}删除失败", key);
        }

        return delete;
    }

    /**
     * 删除多个key
     * 删除指定的key，注意长度必须一致！！！
     *
     * @param redisKeys redisKey对象集合
     * @param params 替换模板的参数
     */
    public void deleteKeys(List<? extends RedisKeyProvider> redisKeys, Object[][] params) {
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
     * 删除多个key
     * 删除指定的key，注意长度必须一致！！！
     *
     * @param redisKeys redisKey对象集合
     * @param params 替换模板的参数
     */
    public void deleteKeys(List<? extends RedisKeyProvider> redisKeys, List<List<Object>> params) {
        AssertUtil.notEmpty(redisKeys, "删除key时,redisKeys不能为空");
        AssertUtil.notEmpty(params, "删除key时,params集合不能为空");

        AssertUtil.isTrue(redisKeys.size() == params.size(),
                String.format("删除redis-key时,参数长度不一致:redisKeys.size:%s,params.size():%s",
                        redisKeys.size(),
                        params.size())
        );

        // 循环删除key
        for (int i = 0; i < redisKeys.size(); i++) {
            this.deleteKey(redisKeys.get(i), params.get(i).toArray(new Object[params.get(i).size()]));
        }
    }

    /**
     * 检查key是否存在
     *
     * @param redisKey redisKey对象
     * @param param 替换模板的参数
     * @return
     */
    public boolean existKey(@Valid RedisKeyProvider redisKey, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        boolean hasKey = super.hasKey(key);
        LogUtil.debug(log, "redis-key:{} {}", key, hasKey ? "存在" : "不存在");
        return hasKey;
    }

    /**
     * 刷新指定key的过期时长
     *
     * @param redisKey redisKey对象
     * @param param 替换模板的参数
     */
    public boolean refresh(RedisKeyProvider redisKey, Object... param) {
        return this.expireByCustom(redisKey, redisKey.getTime(), redisKey.getTimeUnit(), param);
    }

    /**
     * 设置key指定过期时长
     *
     * @param redisKey redisKey对象
     * @param time 过期时长
     * @param timeUnit 过期时长单位
     * @param param 替换参数，避免调用父类的方法
     */
    public boolean expireByCustom(RedisKeyProvider redisKey, long time, @NotNull TimeUnit timeUnit, Object... param) {

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
    public long getExpire(RedisKeyProvider redisKey, Object... param) {
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
     * 类似门面,设置数据到redis,根据{@link RedisKeyProvider#getKey()}和{@link RedisKeyProvider#getJavaType()}进行设置值.
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link RedisKeyProvider#getKey()}的模板参数
     * @return
     */
    public boolean set(@Valid RedisKeyProvider redisKey, Object value, Object... param){
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
     * 设置String类型数据到redis中,允许存储的值：空字符串、null、字符串
     * @see DataType#STRING
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link RedisKeyProvider#getKey()}的模板参数
     * @return
     */
    private boolean setString(RedisKeyProvider redisKey, Object value, Object... param) {
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);

        // 非基本类型需要额外处理成json字符串
        if (!PrimitiveTypeUtil.isBasicType(value)) {
            value = JSON.toJSONString(value);
        }

        if (redisKey.getTime() > 0) {
            super.opsForValue().set(key, value, redisKey.getTime(), redisKey.getTimeUnit());
        } else {
            super.opsForValue().set(key, value);
        }
        return true;
    }

    /**
     * 设置Hash类型数据到redis中,不能是null,且对象中的属性值为null时不设置值.
     * @see DataType#HASH
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link RedisKeyProvider#getKey()}的模板参数
     * @return
     */
    private boolean setHash(RedisKeyProvider redisKey, Object value, Object... param){
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        // 需要将为空的过滤掉
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(value, false, true);
        if (stringObjectMap == null) {
            LogUtil.warn(log, "设置Hash到redis中失败, value为空");
            return false;
        }
        super.opsForHash().putAll(key, stringObjectMap);
        if (redisKey.getTime() > 0) {
            // 设置过期时常
            super.expire(key, redisKey.getTime(), redisKey.getTimeUnit());
        }

        if (redisKey.getJavaType().isInstance(value)) {
            LogUtil.debug(log, "设置Hash到redis中成功, redisType与javaType匹配！");
            return true;
        }

        LogUtil.warn(log, "设置Hash到redis中成功, redisType与javaType不匹配！");
        return false;
    }

    /**
     * 设置List类型数据到redis中，list不能为null也不能为空集合.
     * @see DataType#LIST
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link RedisKeyProvider#getKey()}的模板参数
     * @return
     */
    private boolean setList(RedisKeyProvider redisKey, Object value, Object[] param) {
        // 转换list
        List list = (List)value;

        // Values must not be 'null' or empty.当value为空集合时添加会报错,所以这里判断下
        if (CollectionUtils.isNotEmpty(list)) {
            // 获取完整的 key
            String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
            // 删除key
            if (super.hasKey(key)) {
                super.delete(key);
            }
            // 添加
            super.opsForList().leftPushAll(key, list);
            // 设置过期时间
            if (redisKey.getTime() > 0) {
                super.expire(key, redisKey.getTime(), redisKey.getTimeUnit());
            }
            // 类型比较
            if (redisKey.getJavaType().isInstance(list.get(0))) {
                LogUtil.debug(log, "设置List到redis中成功, redisType与javaType匹配！");
            } else {
                LogUtil.warn(log, "设置List到redis中成功, redisType与javaType不匹配！");
            }

            return true;
        }

        LogUtil.warn(log, "设置List到redis中失败, Values must not be 'null' or empty！");
        return false;
    }

    /**
     * 设置List类型数据到redis中, set为null，空集合都能正常设置.
     * @see DataType#SET
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link RedisKeyProvider#getKey()}的模板参数
     * @return
     */
    private boolean setSet(RedisKeyProvider redisKey, Object value, Object[] param) {
        Set set;
        try {
            set = (Set)value;
        } catch (ClassCastException e) {
            LogUtil.error(log, "设置Set到redis中失败!value:{}, errMsg: ", value, e.getMessage());
            return false;
        }

        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        super.opsForSet().add(key, set);
        if (redisKey.getTime() > 0) {
            super.expire(key, redisKey.getTime(), redisKey.getTimeUnit());
        }

        if (CollectionUtils.isNotEmpty(set)) {
            Object obj = set.stream().findFirst().get();
            // 类型比较
            if (redisKey.getJavaType().isInstance(obj)) {
                LogUtil.debug(log, "设置Set到redis中成功, redisType与javaType匹配！");
            } else {
                LogUtil.warn(log, "设置Set到redis中失败, redisType与javaType不匹配！");
            }
        }


        return true;
    }


    /**
     * 类似门面,设置数据到redis,根据{@link RedisKeyProvider#getKey()}和{@link RedisKeyProvider#getJavaType()}进行获取值.
     * @param redisKey redis-key对象
     * @param param 模板字符串的参数，用于替换{@link RedisKeyProvider#getKey()}的模板参数
     * @return
     */
    public Object get(@Valid RedisKeyProvider redisKey, Object... param){
        DataType dataType = redisKey.getRedisType();
        Class javaType = redisKey.getJavaType();
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
        switch (dataType) {
            case STRING:
                return getString(redisKey, param);
            case HASH:
                return BeanUtil.toBean(super.opsForHash().entries(key), javaType);
            case LIST:
                return getList(redisKey, redisKey.getJavaType(), param);
            case SET:
                return getSet(redisKey, redisKey.getJavaType(), param);
            default:
                String serverMessage = String.format("暂不支持redis设置【%s】类型的数据", dataType);
                throw new RedisToolException(ServerExceptionEnum.SERVER_ERROR, serverMessage);
        }
    }
    /**
     * 获取 dataType 为String的value
     *
     * @param redisKey
     * @param param
     * @return
     */
    public String getString(RedisKeyProvider redisKey, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);

        return (String) super.opsForValue().get(key);
    }

    /**
     * 获取 dataType 为Hash的所有元素
     *
     * @param <T> 返回的类型
     * @param redisKey
     * @param clazz
     * @param param
     * @return
     */
    public <T> T getHash(RedisKeyProvider redisKey, Class<T> clazz, Object... param) {
        if (Objects.equals(clazz, redisKey.getJavaType())) {
            // 获取完整的 key
            String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
            // 将Map转为Bean
            Map map = super.opsForHash().entries(key);
            if (map == null || map.isEmpty()) {
                return null;
            }
            return BeanUtil.toBean(super.opsForHash().entries(key), clazz);
        }

        throw new RedisToolException(ServerExceptionEnum.SERVER_ERROR, "类型不正确");
    }

    /**
     * 获取 dataType 为List的所有元素
     *
     * @param redisKey
     * @param param
     * @return
     */
    public <T> List<T> getList(RedisKeyProvider redisKey, Class<T> clazz, Object... param) {
        if (Objects.equals(clazz, redisKey.getJavaType())) {
            // 获取完整的 key
            String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);
            // 获取list（此时元素是LinkedHashMap）
            List range = super.opsForList().range(key, 0, -1);

            return BeanUtil.copyToList(range, clazz, CopyOptions.create());
        }

        throw new RedisToolException(ServerExceptionEnum.SERVER_ERROR, "类型不正确");
    }

    /**
     * 获取 dataType 为Set的所有元素
     *
     * @param redisKey
     * @param param
     * @return
     */
    public <T> Set<T> getSet(RedisKeyProvider redisKey,  Class<T> clazz, Object... param) {
        // 获取完整的 key
        String key = GenerateRedisKeyUtil.generateByClever(redisKey, param);

        return super.opsForSet().members(key);
    }

    public static void main(String[] args) {
        String 吃 = StrUtil.format("陈飞龙 {ad}岁，早饭{}了", "18", "吃");
        System.out.println("吃 = " + 吃);
    }
}
