package com.goudong.boot.redis.core;

import lombok.Getter;
import org.springframework.data.redis.connection.DataType;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redisKey的公共属性和行为定义，之所以有了枚举{@link RedisKeyTemplateProviderEnum}过后，还编写该类的原因是，强制某些规则，使其子类必须、
 * 遵循某些规则，例如构造函数。这样在后期使用时，才会不会因为用户的错误操作导致一部分功能执行不成功。
 *
 * 举个例子：在redisTool在解决缓存雪崩问题时，有些参数规则必须要存在。
 *
 * 使用方式，只需要继承该类即可，然后使用, 例如{@link SimpleRedisKey}
 *
 * 使用枚举比较好 {@link RedisKeyTemplateProviderEnum}
 * @Author e-Feilong.Chen
 * @Date 2022/1/10 13:34
 */
@Getter
public abstract class AbstractRedisKey implements RedisKeyProvider{

    //~fields
    //==================================================================================================================

    /**
     * redis-key模板字符串，使用`${}`包裹需替换的字符串.
     * @NotBlank
     */
    public String key;

    /**
     * 保存到redis中的数据类型,默认是String
     * @NotNull
     * @see DataType
     */

    public DataType redisType;

    /**
     * 使用RedisTool工具类，在获取key数据后，将其转为javaType
     * 当在redis中存储一个用户列表时,示例：
     * redisType=list
     * javaType=用户对象
     * @NotNull
     */
    public Class javaType;

    /**
     * redis-key 过期时长, 默认-1,当值小于0时，表示不设置失效时间.
     */
    public long time = -1;

    /**
     * redis-key 过期时长单位
     */
    public TimeUnit timeUnit;

    //~construct methods
    //==================================================================================================================

    /**
     * 构造函数，设置key不过期
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     */
    protected AbstractRedisKey(String key, DataType redisType, Class javaType){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
    }

    /**
     * 构造函数，设置key指定时间过期
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     * @param time 过期时长
     * @param timeUnit 过期时长单位，不能为空，因为过期时间设置需要它进行转换时间
     */
    protected AbstractRedisKey(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
        this.time = time;
        this.timeUnit = Optional.ofNullable(timeUnit).orElseGet(()->TimeUnit.SECONDS);
    }
}
