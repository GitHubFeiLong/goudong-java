package com.goudong.commons.enumerate;

import lombok.Getter;
import org.springframework.data.redis.connection.DataType;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redis 的key，及 过期时间，及时间的单位
 * @Author msi
 * @Date 2021/1/7 11:21
 * @Version 1.0
 */
@Getter
public enum RedisKeyEnum {

    /**
     * 重复提交uri记录
     * @param ${uri} 请求地址; ${userUuid} 用户uuid
     */
    REPEAT_URI("gd:repeat:${uri}:${userUuid}", 2, TimeUnit.SECONDS, DataType.STRING),

    /*====================== goudong-oauth2-server ======================*/
    /**
     * 认证服务中的登录信息，保存登录用户的token
     * @param ${uuid} 登录人的主键
     */
    OAUTH2_TOKEN_INFO("gd:oauth2:login-info:token:${uuid}", DataType.STRING),

    /**
     * 认证服务中的 忽略资源
     */
    OAUTH2_IGNORE_RESOURCE("gd:oauth2:ignore-resource", DataType.LIST),

    /**
     * 认证服务中的 用户能访问菜单集合
     * @param uuid 用户uuid
     */
    OAUTH2_USER_MENU("gd:oauth2:user-menu:${uuid}", DataType.LIST),

    /*====================== goudong-message-server ======================*/
    /**
     * 消息服务中的验证码，保存邮箱验证码和短信验证码
     * @param ${email|phone} 邮箱或者手机号
     */
    MESSAGE_AUTH_CODE("gd:message:email-phone-code:${email|phone}", 5, TimeUnit.MINUTES, DataType.STRING),


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
}
