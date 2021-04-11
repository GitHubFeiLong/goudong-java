package com.goudong.commons.enumerate;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * redis 的key，及 过期时间，及时间的单位
 * @ClassName RedisKeyExpirationEnum
 * @Author msi
 * @Date 2021/1/7 11:21
 * @Version 1.0
 */
public enum RedisKeyExpirationEnum {

    /**
     * OAUTH2 服务，需要有系统指定的角色才能访问的url
     */
    OAUTH2_ROLE_VISIT_URL("oauth2:role_visit_url", -1),

    /**
     * OAUTH2 服务，存储用户和角色信息
     * ${user_uuid}：用户uuid
     */
    OAUTH2_USER_ROLE("oauth2:user_role:${user_uuid}")
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

    /**
     * 临时过期时间
     */
    private int tempTime;
    /**
     * 临时过期时间单位
     */
    private TimeUnit tempTimeUnit = TimeUnit.MINUTES;

    RedisKeyExpirationEnum(String key, int time, TimeUnit timeUnit) {
        this.key = key;
        this.time = time;
        this.timeUnit = timeUnit;
    }
    RedisKeyExpirationEnum(String key) {
        this.key = key;
    }
    RedisKeyExpirationEnum(String key, int time) {
        this.key = key;
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public int getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTempTime(int tempTime) {
        this.tempTime = tempTime;
    }

    public void setTempTimeUnit(TimeUnit tempTimeUnit) {
        this.tempTimeUnit = tempTimeUnit;
    }
}
