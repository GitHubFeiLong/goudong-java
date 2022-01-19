package com.goudong.oauth2.core;

import lombok.Data;

import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * 令牌失效配置
 * @author msi
 * @version 1.0
 * @date 2022/1/19 19:40
 */
@Data
public class TokenExpires {

    //~fields
    //==================================================================================================================
    /**
     * 访问token的有效时长
     */
    private Integer access;

    /**
     * 访问token的有效时长单位
     */
    private TimeUnit accessTimeUnit;

    /**
     * 刷新token的有效时长，默认值是 access 的两倍
     */
    private Integer refresh;

    /**
     * 刷新token的有效时长单位
     */
    private TimeUnit refreshTimeUnit;

    //~methods
    //==================================================================================================================

    public TokenExpires(Integer access, TimeUnit accessTimeUnit) {
        this.access = access;
        this.accessTimeUnit = accessTimeUnit;
        // 默认刷新令牌是访问令牌的两倍时常
        this.refresh = 2 * access;
        this.refreshTimeUnit = accessTimeUnit;
    }
}