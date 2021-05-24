package com.goudong.commons.utils;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.ClientExceptionEnumInterface;
import com.goudong.commons.exception.BasicException;
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
     * 请求携带的token的请求头/响应头
     */
    public static final String TOKEN_HEADER = "Authorization";

    /**
     * Bearer token字符串前缀
     */
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";

    /**
     * Basic Base64字符串前缀
     */
    public static final String TOKEN_BASIC_PREFIX = "Basic ";

    /**
     * 作者
     */
    public static final String ISSUER = "cfl";
    /**
     * 有效时长单位小时
     */
    public static final int VALID_HOUR = 7 * 24;

    /**
     * 生产token的盐
     */
    public static final String SALT = "qaqababa";

    @Autowired
    public HttpServletRequest httpServletRequest;

    /**
     * 生产短期的token字符串
     * 将用户的基本信息、权限信息、能访问的菜单信息存储到token中
     * @param authorityUserDTO 用户登录信息
     * @return
     */
    public static String generateToken (AuthorityUserDTO authorityUserDTO, int hour) {
        // secret 密钥，只有服务器知道
        Algorithm algorithm = Algorithm.HMAC256(JwtTokenUtil.SALT);
        // 当前时间
        LocalDateTime ldt = LocalDateTime.now();

        String token = JWT.create()
                // jwt唯一id
                .withJWTId(IdUtil.randomUUID())
                // 发布者
                .withIssuer(JwtTokenUtil.ISSUER)
                // 生成签名的时间
                .withIssuedAt(new Date())
                // 有效时长
                .withExpiresAt(Date.from(ldt.plusHours(hour).atZone(ZoneId.systemDefault()).toInstant()))
                // 绑定用户数据
                .withAudience(JSON.toJSONString(authorityUserDTO))
                // 主题
                .withSubject("狗东")
                // 签发的目标
                .sign(algorithm);

        return TOKEN_BEARER_PREFIX + token;
    }



    /**
     * 解析token字符串
     * @param token 需要被解析的字符串
     */
    public static AuthorityUserDTO resolveToken(String token) {

        Algorithm algorithm = Algorithm.HMAC256(JwtTokenUtil.SALT);
        JWTVerifier verifier = JWT.require(algorithm)
                // //匹配指定的token发布者
                .withIssuer(JwtTokenUtil.ISSUER)
                .build();
        DecodedJWT jwt = verifier.verify(token);
        String result = jwt.getAudience().get(0);
        log.info("result:{}",result);
        return JSON.parseObject(result, AuthorityUserDTO.class);
    }

    /**
     * 根据token 返回用户uuid
     * @param request
     * @return
     */
    public static String getUserUuid(HttpServletRequest request) {
        String tokenHeader = request.getHeader(JwtTokenUtil.TOKEN_HEADER);

        // token 格式不对
        boolean isFormatError = tokenHeader == null
                || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_BEARER_PREFIX)
                || !tokenHeader.startsWith(JwtTokenUtil.TOKEN_BASIC_PREFIX);
        if (isFormatError) {
            BasicException.exception(ClientExceptionEnumInterface.TOKEN_ERROR);
        }

        String token = null;
        // 去掉前面的 "Bearer " 字符串
        if (tokenHeader.startsWith(JwtTokenUtil.TOKEN_BEARER_PREFIX)) {
            token = tokenHeader.replace(JwtTokenUtil.TOKEN_BEARER_PREFIX, "");
        } else if (tokenHeader.startsWith(JwtTokenUtil.TOKEN_BASIC_PREFIX)) {
            token = tokenHeader.replace(JwtTokenUtil.TOKEN_BEARER_PREFIX, "");
        } else {
            // 兜底方法
        }


        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);

        return authorityUserDTO.getUuid();
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
