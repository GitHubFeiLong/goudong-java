package com.goudong.commons.enumerate.core;

import com.goudong.commons.frame.redis.AbstractRedisKey;
import com.goudong.commons.pojo.IgnoreResourceAntMatcher;
import lombok.Getter;
import org.springframework.data.redis.connection.DataType;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redis 的key，及 过期时间，及时间的单位
 * @deprecated 这个不太好扩展,并且所有key都放在一个枚举里面，不太好分类管理，推荐使用新版的方式{@link AbstractRedisKey}
 *
 * @Author msi
 * @Date 2021/1/7 11:21
 * @Version 1.0
 */
@Getter
@Deprecated
public enum RedisKeyEnum {


    /**
     * 重复提交uri记录
     * @param ${uri} 请求地址;
     * @param ${userId} 用户id
     */
    REPEAT_URI("gd:repeat:${uri}:${userId}", 2, TimeUnit.SECONDS, DataType.STRING),

    /*====================== goudong-oauth2-server ======================*/
    /**
     * 存用户登录token信息
     * 作用：判断是否在线。
     * @param ${id} 登录人的主键
     */
    OAUTH2_TOKEN_INFO("gd:oauth2:login-info:token-info:${id}", DataType.STRING),

    /**
     * 存用户详细信息
     * @param ${token-md5} token字符串转成16进制16位的字符串
     */
    OAUTH2_USER_INFO("gd:oauth2:login-info:user-info:${token-md5}", DataType.HASH),

    /**
     * 认证服务中的 忽略资源
     */
    OAUTH2_IGNORE_RESOURCE("gd:oauth2:ignore-resource", DataType.LIST, IgnoreResourceAntMatcher.class),

    /**
     * 添加菜单资源时，使用redisson加分布式锁
     */
    OAUTH2_REDISSON_ADD_MENU("gd:oauth2:redisson:add-menu", 3, TimeUnit.SECONDS, DataType.STRING),

    /**
     * 设置redis中的白名单，使用redisson加分布式锁
     */
    OAUTH2_REDISSON_IGNORE_RESOURCE("gd:oauth2:redisson:ignore-resource", 3, TimeUnit.SECONDS, DataType.STRING),

    /*====================== goudong-message-server ======================*/
    /**
     * 消息服务中的验证码，保存邮箱验证码和短信验证码
     * @param ${email|phone} 邮箱或者手机号
     */
    MESSAGE_AUTH_CODE("gd:message:email-phone-code:${email|phone}", 10, TimeUnit.MINUTES, DataType.STRING, String.class),

    ;
    /**
     * redis key template
     */
    private String key;
    /**
     * redis的value数据类型
     * 默认是String
     * @see DataType
     */
    private DataType dataType = DataType.STRING;
    /**
     * redis存储数据的元数据java类型
     * 默认是String
     */
    private Class clazz = String.class;
    /**
     * redis key 时间，默认3
     * 注意：当值小于0时，表示不设置失效时间
     */
    private int time = 3;
    /**
     * reids key 时间单位 默认小时
     */
    private TimeUnit timeUnit = TimeUnit.HOURS;

    RedisKeyEnum(String key){
        this.key = key;
    }
    RedisKeyEnum(String key, int time) {
        this.key = key;
        this.time = time;
    }
    RedisKeyEnum(String key, DataType dataType){
        this.key = key;
        this.dataType = dataType;
    }
    RedisKeyEnum(String key, DataType dataType, Class clazz){
        this.key = key;
        this.dataType = dataType;
        this.clazz = clazz;
    }
    RedisKeyEnum(String key, int time, TimeUnit timeUnit) {
        this.key = key;
        this.time = time;
        this.timeUnit = timeUnit;
    }
    RedisKeyEnum(String key, int time, TimeUnit timeUnit, DataType dataType) {
        this.key = key;
        this.time = time;
        this.timeUnit = timeUnit;
        this.dataType = dataType;
    }
    RedisKeyEnum(String key, int time, TimeUnit timeUnit, DataType dataType, Class clazz) {
        this.key = key;
        this.time = time;
        this.timeUnit = timeUnit;
        this.dataType = dataType;
        this.clazz = clazz;
    }
}
