package com.goudong.commons.utils;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;

import javax.annotation.Resource;
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

    @Resource
    private ResourceProperties resourceProperties;

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

        // 将加工后的转换原生token（没有Bearer ，Basic 字符串）
        String nativeToken = generateNativeToken(token);

        Algorithm algorithm = Algorithm.HMAC256(JwtTokenUtil.SALT);
        JWTVerifier verifier = JWT.require(algorithm)
                // //匹配指定的token发布者
                .withIssuer(JwtTokenUtil.ISSUER)
                .build();

        DecodedJWT jwt = null;
        try {
            jwt = verifier.verify(nativeToken);
        } catch (TokenExpiredException | JWTDecodeException e) {
            BasicException.exception(ClientExceptionEnum.UNAUTHORIZED);
        }

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

        String token = generateNativeToken(tokenHeader);

        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);

        return authorityUserDTO.getUuid();
    }

    /**
     * 生成原生的token字符串，参数包含了 Bearer 或 Basic 开头
     * @param token
     * @return
     */
    private static String generateNativeToken (String token) {
        // token 格式不对
        boolean isFormatError = token == null
                || !(token.startsWith(JwtTokenUtil.TOKEN_BEARER_PREFIX) || token.startsWith(JwtTokenUtil.TOKEN_BASIC_PREFIX));
        if (isFormatError) {
            BasicException.exception(ClientExceptionEnum.TOKEN_ERROR);
        }
        // 去掉前面的 "Bearer " 字符串
        if (token.startsWith(JwtTokenUtil.TOKEN_BEARER_PREFIX)) {
            return token.replace(JwtTokenUtil.TOKEN_BEARER_PREFIX, "");
        }
        // 去掉前面的 "Basic "字符串
        if (token.startsWith(JwtTokenUtil.TOKEN_BASIC_PREFIX)) {
            return token.replace(JwtTokenUtil.TOKEN_BEARER_PREFIX, "");
        }

        return null;
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
