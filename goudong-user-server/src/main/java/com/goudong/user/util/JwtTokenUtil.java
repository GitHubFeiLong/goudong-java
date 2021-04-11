package com.goudong.user.util;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.goudong.user.entity.AuthorityUserDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * 类描述：
 *
 * @ClassName TokenUtil
 * @Author msi
 * @Date 2020/6/12 19:57
 * @Version 1.0
 */
@Slf4j
public class JwtTokenUtil {
    /**
     * 短期token的响应头
     */
    public static final String TOKEN = "token";
    /**
     * 长期token的响应头
     */
    public static final String REFRESH_TOKEN = "refreshToken";
    /**
     * 请求携带的请求头
     */
    public static final String TOKEN_HEADER = "Authorization";
    /**
     * token字符串前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 作者
     */
    public static final String ISSUER = "cfl";
    /**
     * 短期有效时长单位小时
     */
    public static final int VALID_SHORT_TERM_HOUR = 2;
    /**
     * 长期有效时长单位小时
     */
    public static final int VALID_LONG_TERM_HOUR = 24*7;

    /**
     * 生产token的盐
     */
    public static final String SALT = "qaqababa";

    @Autowired
    public HttpServletRequest httpServletRequest;

    /**
     * 生产短期的token字符串
     * 将用户的基本信息、权限信息、能访问的菜单信息存储到token中
     * @param authorityUserDO 用户登录信息
     * @return
     */
    public static String generateToken (AuthorityUserDO authorityUserDO, int hour) {
        Algorithm algorithm = Algorithm.HMAC256(JwtTokenUtil.SALT); // secret 密钥，只有服务器知道
        // 当前时间
        LocalDateTime ldt = LocalDateTime.now();

        return JWT.create()
                // 发布者
                .withIssuer(JwtTokenUtil.ISSUER)
                // 生成签名的时间
                .withIssuedAt(new Date())
                // 有效时长
                .withExpiresAt(Date.from(ldt.plusHours(hour).atZone(ZoneId.systemDefault()).toInstant()))
                // 绑定用户数据
                .withAudience(JSON.toJSONString(authorityUserDO))
                .sign(algorithm);
    }



    /**
     * 解析token字符串
     * @param token 需要被解析的字符串
     */
    public static AuthorityUserDO resolveToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(JwtTokenUtil.SALT);
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer(JwtTokenUtil.ISSUER) //匹配指定的token发布者
                .build();
        DecodedJWT jwt = verifier.verify(token);
        String result = jwt.getAudience().get(0);
        log.info("result:{}",result);
        return JSON.parseObject(result, AuthorityUserDO.class);
    }

    // 从token中获取用户名
//    public static String getUsername(String token){
//        return getTokenBody(token).getSubject();
//    }
//
//    // 是否已过期
//    public static boolean isExpiration(String token){
//        return getTokenBody(token).getExpiration().before(new Date());
//    }

}
