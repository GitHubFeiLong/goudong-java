package com.goudong.authentication.common.core;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

/**
 * 类描述：
 * 用户登录后的token
 * @Author Administrator
 * @Version 1.0
 */
@Data
public class Token {
    //~fields
    //==================================================================================================================
    /**
     * 访问令牌
     */
    private String accessToken;
    /**
     * 刷新令牌
     */
    private String refreshToken;
    /**
     * accessToken失效时长
     */
    private Date accessExpires;
    /**
     * refreshToken失效时长
     */
    private Date refreshExpires;

    //~methods
    //==================================================================================================================
}
