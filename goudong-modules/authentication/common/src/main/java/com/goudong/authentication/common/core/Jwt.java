package com.goudong.authentication.common.core;

import com.goudong.authentication.common.util.JsonUtil;
import com.goudong.boot.web.core.ClientException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：
 * jwt创建和解析
 * @author cfl
 * @version 1.0
 * @date 2023/7/19 17:00
 */
public class Jwt {

    /**
     * 游戏时长
     */
    private long time;

    /**
     * 时长单位
     */
    private TimeUnit timeUnit;

    /**
     * 密钥
     */
    private String secretKey;

    public Jwt(String secretKey) {
        try {
            this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * 构造方法，创建jwt实例
     * @param time
     * @param timeUnit
     * @param secretKey
     */
    public Jwt(long time, TimeUnit timeUnit, String secretKey) {
        this.time = time;
        this.timeUnit = timeUnit;
        try {
            this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 创建token
     * @param userSimple
     * @return
     */
    public Token generateToken(UserSimple userSimple) {
        Date now = new Date();
        long millis = this.timeUnit.toMillis(time);
        Date accessExpiration = new Date(now.getTime() + millis);
        Date refreshExpiration = new Date(now.getTime() + millis * 2);
        String json = JsonUtil.toJsonString(userSimple);
        String accessToken = Jwts.builder()
                .setSubject(json)
                .setIssuedAt(now)
                .setExpiration(accessExpiration)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(json)
                .setIssuedAt(now)
                .setExpiration(refreshExpiration)
                .signWith(SignatureAlgorithm.HS256, this.secretKey)
                .compact();

        Token token = new Token();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setAccessExpires(accessExpiration);
        token.setRefreshExpires(refreshExpiration);

        return token;
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public UserSimple parseToken(String token) {
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(this.secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return JsonUtil.toObject(body.getSubject(), UserSimple.class);
        } catch (ExpiredJwtException e) {
            throw ClientException.clientByUnauthorized();
        }
    }

}
