package com.zhy.authentication.common.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.io.IOException;
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
     * @param userToken
     * @return
     */
    public String generateToken(UserToken userToken) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + this.timeUnit.toMillis(time));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String userTokenJson = objectMapper.writeValueAsString(userToken);

            return Jwts.builder()
                    .setSubject(userTokenJson)
                    .setIssuedAt(now)
                    .setExpiration(expiration)
                    .signWith(SignatureAlgorithm.HS256, this.secretKey)
                    .compact();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public UserToken parseToken(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Claims body = Jwts.parser()
                    .setSigningKey(this.secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return objectMapper.readValue(body.getSubject(), UserToken.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
