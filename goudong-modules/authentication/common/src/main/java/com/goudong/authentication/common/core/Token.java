package com.goudong.authentication.common.core;

import java.util.Date;
import java.util.Objects;

/**
 * 类描述：
 * 用户登录后的token
 * @Author Administrator
 * @Version 1.0
 */
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

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getAccessExpires() {
        return accessExpires;
    }

    public void setAccessExpires(Date accessExpires) {
        this.accessExpires = accessExpires;
    }

    public Date getRefreshExpires() {
        return refreshExpires;
    }

    public void setRefreshExpires(Date refreshExpires) {
        this.refreshExpires = refreshExpires;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token = (Token) o;
        return Objects.equals(accessToken, token.accessToken) && Objects.equals(refreshToken, token.refreshToken) && Objects.equals(accessExpires, token.accessExpires) && Objects.equals(refreshExpires, token.refreshExpires);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, refreshToken, accessExpires, refreshExpires);
    }

    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", accessExpires=" + accessExpires +
                ", refreshExpires=" + refreshExpires +
                '}';
    }
}
