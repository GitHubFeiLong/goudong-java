package com.goudong.oauth2.enumerate;

import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO2Redis;
import com.goudong.commons.framework.redis.RedisKeyProvider;
import com.goudong.oauth2.dto.BaseMenuDTO2Redis;
import org.springframework.data.redis.connection.DataType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * oauth2服务的redis key
 * 参数使用`${}`进行包裹，例如"user:${user-id}",RedisTool会动态设置参数值
 * @author msi
 * @date 2022/1/20 19:51
 * @version 1.0
 */
public enum RedisKeyProviderEnum implements RedisKeyProvider {

    /**
     * 白名单
     */
    WHITELIST("goudong-oauth2-server:whitelist", DataType.LIST, BaseWhitelistDTO2Redis.class, 1, TimeUnit.DAYS),

    /**
     * 认证成的放进redis，由于对象比较复杂，使用json字符串存储更合适
     * @see
     * @param client_side 客户端类型
     * @param access_token 访问令牌 uuid
     */
    AUTHENTICATION("goudong-oauth2-server:authentication:${client_side}:${access_token}", DataType.STRING, BaseUserDTO.class, 2, TimeUnit.HOURS),

    /**
     * 将所有菜单数据保存到redis
     */
    MENU_ALL("goudong-oauth2-server:menu:ALL", DataType.LIST, BaseMenuDTO2Redis.class, 1, TimeUnit.DAYS),

    /**
     * 将角色对应的菜单保存到redis
     * @param role 角色英文名
     */
    MENU_ROLE("goudong-oauth2-server:menu:${role}", DataType.LIST, BaseMenuDTO2Redis.class, 1, TimeUnit.DAYS)
    ;
    //~fields
    //==================================================================================================================

    /**
     * redis-key模板字符串，使用`${}`包裹需替换的字符串.
     */
    @NotBlank
    public String key;

    /**
     * 保存到redis中的数据类型,默认是String
     *
     * @see DataType
     */
    @NotNull
    public DataType redisType;

    /**
     * 使用RedisTool工具类，在获取key数据后，将其转为javaType
     * 当在redis中存储一个用户列表时,示例：
     * redisType=list
     * javaType=用户对象
     */
    @NotNull
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
     *
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     */
    RedisKeyProviderEnum(String key, DataType redisType, Class javaType){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
    }

    /**
     *
     * @param key key模板字符串
     * @param redisType redis数据类型
     * @param javaType java数据类型
     * @param time 过期时长
     * @param timeUnit 过期时长单位
     */
    RedisKeyProviderEnum(String key, DataType redisType, Class javaType, long time, TimeUnit timeUnit){
        this.key = key;
        this.redisType = redisType;
        this.javaType = javaType;
        this.time = time;
        this.timeUnit = timeUnit;
    }

    //~methods
    //==================================================================================================================
    @Override
    public String getKey() {
        return key;
    }

    @Override
    public DataType getRedisType() {
        return redisType;
    }

    @Override
    public Class getJavaType() {
        return javaType;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return timeUnit;
    }
}
