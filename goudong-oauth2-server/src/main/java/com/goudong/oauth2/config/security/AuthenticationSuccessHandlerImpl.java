package com.goudong.oauth2.config.security;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.core.AuthenticationImpl;
import com.goudong.oauth2.core.LoginInfo;
import com.goudong.oauth2.core.TokenExpires;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.properties.TokenExpiresProperties;
import com.goudong.oauth2.service.BaseTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Locale;

/**
 * 类描述：
 * 认证成功处理器
 *
 * @author msi
 * @date 2022/1/15 21:46
 * @version 1.0
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    /**
     * token服务层接口
     */
    private final BaseTokenService baseTokenService;

    /**
     * 令牌过期配置
     */
    private final TokenExpiresProperties tokenExpiresProperties;

    /**
     * redis工具
     */
    private final RedisTool redisTool;

    public AuthenticationSuccessHandlerImpl(BaseTokenService baseTokenService,
                                            TokenExpiresProperties tokenExpiresProperties, RedisTool redisTool) {
        this.baseTokenService = baseTokenService;
        this.tokenExpiresProperties = tokenExpiresProperties;
        this.redisTool = redisTool;
    }

    /**
     *
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication 保存当前用户的登录信息
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        // 转换成自定义的Authentication对象
        AuthenticationImpl authenticationImpl = (AuthenticationImpl) authentication;

        // 获取请求类型
        ClientSideEnum clientSideEnum = ClientSideEnum.getClientSide(httpServletRequest.getHeader(HttpHeaderConst.CLIENT_SIDE));

        final String clientSide = clientSideEnum.name().toLowerCase(Locale.ROOT);

        /*
            创建令牌
         */
        BaseTokenDTO baseTokenDTO = new BaseTokenDTO();
        baseTokenDTO.setAccessToken(IdUtil.simpleUUID());
        baseTokenDTO.setRefreshToken(IdUtil.simpleUUID());
        baseTokenDTO.setUserId(authenticationImpl.getId());
        baseTokenDTO.setClientType(clientSide);
        // 默认是 browser
        TokenExpires tokenExpires = getTokenExpires(clientSideEnum);

        // 处理过期时间和类型
        disposeToken(tokenExpires, baseTokenDTO);
        // 持久化token
        BaseTokenDTO tokenDTO = baseTokenService.save(baseTokenDTO);

        /*
            将令牌和用户基本信息存储到redis中
         */
        BaseUserDTO baseUser = BeanUtil.copyProperties(authenticationImpl, BaseUserDTO.class);

        // 保存redis并设置过期时长
        redisTool.set(RedisKeyProviderEnum.AUTHENTICATION, baseUser, clientSide, baseTokenDTO.getAccessToken());
        redisTool.expireByCustom(RedisKeyProviderEnum.AUTHENTICATION, tokenExpires.getAccess(),
                tokenExpires.getAccessTimeUnit(), clientSide, baseTokenDTO.getAccessToken());

        /*
            响应令牌和用户信息
         */
        LoginInfo loginInfo = BeanUtil.copyProperties(tokenDTO, LoginInfo.class);
        loginInfo.setUser(baseUser);
        String json = new ObjectMapper().setDateFormat(new SimpleDateFormat(DateConst.DATE_TIME_FORMATTER))
                .writeValueAsString(loginInfo);

        httpServletResponse.getWriter().write(json);
    }

    /**
     * 获取当前请求的
     * @param clientSideEnum 客户端类型
     * @return
     */
    private TokenExpires getTokenExpires(ClientSideEnum clientSideEnum) {
        TokenExpires tokenExpires;
        switch (clientSideEnum) {
            case APP:
                tokenExpires = tokenExpiresProperties.getApp();
                break;
            case BROWSER:
            default:
                tokenExpires = tokenExpiresProperties.getBrowser();
                break;
        }
        return tokenExpires;
    }
    /**
     * 处理生成token
     * @param baseTokenDTO 处理token对象
     */
    private void disposeToken(TokenExpires tokenExpires, BaseTokenDTO baseTokenDTO) {
        LocalDateTime now = LocalDateTime.now();
        // 设置过期时长
        baseTokenDTO.setAccessExpires(
                Date.from(
                        now.plusSeconds(tokenExpires.getAccessTimeUnit().toSeconds(tokenExpires.getAccess()))
                                .atZone( ZoneId.systemDefault()).toInstant()
                )
        );
        baseTokenDTO.setRefreshExpires(
                Date.from(
                        now.plusSeconds(tokenExpires.getRefreshTimeUnit().toSeconds(tokenExpires.getRefresh()))
                                .atZone( ZoneId.systemDefault()).toInstant()
                )
        );
    }


}
