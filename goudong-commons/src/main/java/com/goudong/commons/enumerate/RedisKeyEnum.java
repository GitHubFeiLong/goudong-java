package com.goudong.commons.enumerate;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

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
     * 消息服务中的验证码，保存邮箱验证码和短信验证码
     * @param ${email|phone} 邮箱或者手机号
     */
    MESSAGE_AUTH_CODE("gd:message:email-phone-code:${email|phone}", 5, TimeUnit.MINUTES)
    ;

    /**
     * redis key template
     */
    private String key;
    /**
     * redis key 时间，默认3
     * 注意：当值小于0时，表示不设置失效时间
     */
    private int time = 3;
    /**
     * reids key 时间单位 默认小时
     */
    private TimeUnit timeUnit = TimeUnit.HOURS;

    RedisKeyEnum(String key, int time, TimeUnit timeUnit) {
        this.key = key;
        this.time = time;
        this.timeUnit = timeUnit;
    }
}
