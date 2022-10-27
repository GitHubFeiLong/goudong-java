package com.goudong.commons.framework.redis;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.RandomUtil;
import com.alibaba.fastjson.JSON;
import com.goudong.commons.annotation.aop.SnowSlideHandler;
import com.goudong.commons.exception.redis.RedisToolException;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.commons.utils.core.PrimitiveTypeUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.*;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * Redis操作RedisKeyProvider,只实现了一部分的简单功能，例如：设置，获取，删除，修改过期时间。其余的需要自己手动调用底层API，例如取交集，并集等
 *
 * @Author e-Feilong.Chen
 * @Date 2022/1/10 15:16
 */
@Slf4j
@Validated
public class RedisTool extends RedisTemplate {

    /**
     * 使用ThreadLocal变量，在其它方法执行时，做其它操作。
     * 方法内判断是否有开启失效时间增加随机秒，然后进行处理
     * 使用ThreadLocal，让某些数据对准时性要求不高时，在给过期时间时，加上一点随机数，避免缓存雪崩的问题。
     *
     * @see com.goudong.commons.aop.SnowSlideHandlerAop 进行清理ThreadLocal
     */
    private static final ThreadLocal<Entry> ENTRY_THREAD_LOCAL = new ThreadLocal<>();

    @Getter
    static class Entry {
        /**
         * 是否启用雪崩处理
         */
        private Boolean enableSnowSlideHandlerSnow = false;

        /**
         * 新增多少秒
         */
        private Long snowSlideHandlerSecond = 0L;

        public Entry(boolean enableSnowSlideHandlerSnow) {
            this.enableSnowSlideHandlerSnow = enableSnowSlideHandlerSnow;
            // 随机增加0~60s
            this.snowSlideHandlerSecond = RandomUtil.randomLong(0, 60);
        }
    }

    //~methods
    //==================================================================================================================

    /**
     * 启动
     * @return
     */
    public RedisTool enableRandom() {
        ENTRY_THREAD_LOCAL.set(new Entry(true));
        return this;
    }

    /**
     * 禁用
     * @return
     */
    public RedisTool disableRandom() {
        ENTRY_THREAD_LOCAL.remove();
        return this;
    }

    /**
     * 获取redis的key
     * @param redisKey
     * @param param
     * @return
     */
    public String getKey(RedisKeyProvider redisKey, Object... param) {
        // 获取完整的 key
        return GenerateRedisKeyUtil.generateByClever(redisKey, param);
    }

    /**
     * 根据key，使用scan获取匹配的所有key，使用scan避免阻塞
     *
     * @return 返回获取到的keys
     */
    public Set<String> getKeyPattern(RedisKeyProvider redisKey) {
        String pattern = redisKey.getKey().replaceAll("\\$\\{.*\\}", "*");

        return (Set<String>) super.execute(connect -> {
            Set<String> binaryKeys = new HashSet<>();
            Cursor<byte[]> cursor = connect.scan(new ScanOptions.ScanOptionsBuilder().match(pattern).count(200000).build());
            while (cursor.hasNext() && binaryKeys.size() < 200000) {
                binaryKeys.add(new String(cursor.next()));
            }
            return binaryKeys;
        }, true);
    }

    /**
     * 删除单个key
     *
     * @param redisKey redisKey对象
     * @param param 替换模板的参数
     * @return 删除成功时返回true,当删除的key不存在时,返回false.
     */
    public boolean deleteKey(RedisKeyProvider redisKey, Object... param) {
        // 获取完整的 key
        String key = getKey(redisKey, param);
        // String key = StringUtil.format(redisKey.getKey(), param);
        // key 不存在时，删除失败，返回false
        boolean delete = super.delete(key);
        if (delete) {
            LogUtil.debug(log, "redis-key:【{}】已被删除", key);
        } else {
            LogUtil.warn(log, "redis-key:【{}】删除失败", key);
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

        // 开启事务批量删除
        super.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                Set<String> delKes = new HashSet<>();
                // 循环删除key
                for (int i = 0; i < redisKeys.size(); i++) {
                    delKes.add(getKey(redisKeys.get(i), params[i]));
                }
                operations.delete(delKes);
                return true;
            }
        });
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

        // 开启事务批量删除
        super.execute(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                Set<String> delKes = new HashSet<>();
                // 循环删除key
                for (int i = 0; i < redisKeys.size(); i++) {
                    delKes.add(getKey(redisKeys.get(i), params.get(i).toArray(new Object[params.get(i).size()])));
                }

                operations.delete(delKes);
                return true;
            }
        });
    }

    /**
     * 根据key模板，进行模糊删除key
     * @param redisKey
     */
    public void deleteKeysByPattern(RedisKeyProvider redisKey) {
        super.delete(getKeyPattern(redisKey));
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
        String key = getKey(redisKey, param);
        boolean hasKey = super.hasKey(key);
        LogUtil.debug(log, "redis-key:【{}】【{}】", key, hasKey ? "存在" : "不存在");
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

        String key = getKey(redisKey, param);

        boolean result = super.expire(key, time, timeUnit);

        if (!result) {
            LogUtil.error(log, "更新redis key过期时间错误（key：【{}】 时长：{} 时间单位：{}） ，该key可能不存在",
                    key,
                    time,
                    timeUnit);

            return false;
        }

        LogUtil.debug(log, "刷新redis-key:【{}】过期时间成功, ttl:{}s", key, timeUnit.toSeconds(time));
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
        String key = getKey(redisKey, param);
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
     * 该方法会将原有的数据进行删除后在设置新的值
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link RedisKeyProvider#getKey()}的模板参数
     * @return
     */
    @SnowSlideHandler
    public boolean set(@Valid RedisKeyProvider redisKey, Object value, Object... param){
        DataType dataType = redisKey.getRedisType();
        switch (dataType) {
            case STRING:
                return setString(redisKey, value, param);
            case HASH:
                return setHash(redisKey, value, param);
            case LIST:
                return setList(redisKey, value, param);
            case SET:
                return setSet(redisKey, value, param);
            case ZSET:
                return setZSet(redisKey, value, param);
            default:
                String serverMessage = String.format("暂不支持redis设置【%s】类型的数据", dataType);
                throw new RedisToolException(serverMessage);
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
        String key = getKey(redisKey, param);

        // 非基本类型需要额外处理成json字符串
        if (!PrimitiveTypeUtil.isBasicType(value)) {
            value = JSON.toJSONString(value);
        }

        // 获取过期时间单位秒
        long second = redisKey.getTime2Second(ENTRY_THREAD_LOCAL.get());
        if (second > 0) {
            super.opsForValue().set(key, value, second, TimeUnit.SECONDS);
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
        final String key = getKey(redisKey, param);
        // 需要将为空的过滤掉
        Map<String, Object> stringObjectMap = BeanUtil.beanToMap(value, false, true);
        if (stringObjectMap == null) {
            LogUtil.warn(log, "设置Hash到redis中失败, value为空");
            return false;
        }
        // 获取过期时间单位秒
        long second = redisKey.getTime2Second(ENTRY_THREAD_LOCAL.get());
        super.execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                // 开启事务
                operations.multi();
                // 删除key
                operations.delete(key);

                operations.opsForHash().putAll(key, stringObjectMap);
                if (second > 0) {
                    // 设置过期时常
                    operations.expire(key, second, TimeUnit.SECONDS);
                }
                // 提交
                operations.exec();
                return true;
            }
        });

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
            String key = getKey(redisKey, param);
            // 获取过期时间单位秒
            long second = redisKey.getTime2Second(ENTRY_THREAD_LOCAL.get());

            super.execute(new SessionCallback<Boolean>() {
                @Override
                public Boolean execute(RedisOperations operations) throws DataAccessException {
                    // 开启事务
                    operations.multi();
                    // 删除key
                    operations.delete(key);
                    // 添加
                    operations.opsForList().rightPushAll(key, list);
                    if (second > 0) {
                        // 设置过期时常
                        operations.expire(key, second, TimeUnit.SECONDS);
                    }
                    // 提交
                    operations.exec();
                    return true;
                }
            });

            // 类型比较
            if (redisKey.getJavaType().isInstance(list.get(0))) {
                LogUtil.debug(log, "设置List到redis中成功, redisType与javaType匹配！");
            } else {
                LogUtil.warn(log, "设置List到redis中成功, redisType与javaType不匹配！");
            }

            return true;
        }

        LogUtil.error(log, "设置List到redis中失败, Values must not be 'null' or empty！");
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
        String key = getKey(redisKey, param);
        // 获取过期时间单位秒
        long second = redisKey.getTime2Second(ENTRY_THREAD_LOCAL.get());
        super.execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                // 开启事务
                operations.multi();
                // 删除key
                operations.delete(key);
                // 添加
                operations.opsForSet().add(key, set);
                if (second > 0) {
                    // 设置过期时常
                    operations.expire(key, second, TimeUnit.SECONDS);
                }
                // 提交
                operations.exec();
                return true;
            }
        });

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
     * 设置ZSet类型数据到redis中
     * @see DataType#ZSET
     * @param redisKey redis-key对象
     * @param value 需要被保存的值
     * @param param 模板字符串的参数，用于替换{@link RedisKeyProvider#getKey()}的模板参数
     * @return
     */
    private boolean setZSet(RedisKeyProvider redisKey, Object value, Object[] param) {
        Set<DefaultTypedTuple> zset;
        try {
            zset = (Set<DefaultTypedTuple>)value;
        } catch (ClassCastException e) {
            LogUtil.error(log, "设置ZSet到redis中失败!value:{}, errMsg: ", value, e.getMessage());
            return false;
        }

        // 获取完整的 key
        String key = getKey(redisKey, param);
        // 获取过期时间单位秒
        long second = redisKey.getTime2Second(ENTRY_THREAD_LOCAL.get());
        super.execute(new SessionCallback<Boolean>() {
            @Override
            public Boolean execute(RedisOperations operations) throws DataAccessException {
                // 开启事务
                operations.multi();
                // 删除key
                operations.delete(key);
                // 添加
                operations.opsForZSet().add(key, zset);
                if (second > 0) {
                    // 设置过期时常
                    operations.expire(key, second, TimeUnit.SECONDS);
                }
                // 提交
                operations.exec();
                return true;
            }
        });

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
        String key = getKey(redisKey, param);
        switch (dataType) {
            case STRING:
                return getString(redisKey, param);
            case HASH:
                return getHash(redisKey, redisKey.getJavaType(), param);
            case LIST:
                return getList(redisKey, redisKey.getJavaType(), param);
            case SET:
                return getSet(redisKey, redisKey.getJavaType(), param);
            case ZSET:
                return getZSet(redisKey, redisKey.getJavaType(), param);
            default:
                String serverMessage = String.format("暂不支持redis设置【%s】类型的数据", dataType);
                throw new RedisToolException(serverMessage);
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
        String key = getKey(redisKey, param);

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
            String key = getKey(redisKey, param);
            // 将Map转为Bean
            Map map = super.opsForHash().entries(key);
            if (map == null || map.isEmpty()) {
                return null;
            }
            return BeanUtil.toBean(super.opsForHash().entries(key), clazz);
        }

        throw new RedisToolException("类型不正确");
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
            String key = getKey(redisKey, param);
            // 获取list（此时元素是LinkedHashMap）
            List range = super.opsForList().range(key, 0, -1);

            return BeanUtil.copyToList(range, clazz, CopyOptions.create());
        }

        throw new RedisToolException("类型不正确");
    }

    /**
     * 获取 dataType 为Set的所有元素
     *
     * @param redisKey
     * @param param
     * @return
     */
    public <T> Set<T> getSet(RedisKeyProvider redisKey, Class<T> clazz, Object... param) {
        // 获取完整的 key
        String key = getKey(redisKey, param);
        return super.opsForSet().members(key);
    }

    /**
     * 获取 dataType 为ZSet的所有元素
     *
     * @param redisKey
     * @param param
     * @return
     */
    public <T> Set<T> getZSet(RedisKeyProvider redisKey, Class<T> clazz, Object... param) {
        // 获取完整的 key
        String key = getKey(redisKey, param);
        Set<T> set = new HashSet();
        super.opsForZSet().rangeWithScores(key, 0, -1).forEach(v->{
            set.add((T)v);
        });

        return set;
    }

}
