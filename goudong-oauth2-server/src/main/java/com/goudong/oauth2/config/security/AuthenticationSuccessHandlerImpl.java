package com.goudong.oauth2.config.security;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.core.LoginInfo;
import com.goudong.oauth2.core.TokenExpires;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.po.BaseTokenPO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.properties.TokenExpiresProperties;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Example;
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
     * 用户服务
     */
    private final BaseUserService baseUserService;

    /**
     * redis tool
     */
    private final RedisTool redisTool;

    public AuthenticationSuccessHandlerImpl(BaseTokenService baseTokenService,
                                            TokenExpiresProperties tokenExpiresProperties,
                                            BaseUserService baseUserService,
                                            RedisTool redisTool) {
        this.baseTokenService = baseTokenService;
        this.tokenExpiresProperties = tokenExpiresProperties;
        this.baseUserService = baseUserService;
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
        BaseUserPO baseUserPO = (BaseUserPO) authentication;

        // 获取请求类型
        ClientSideEnum clientSideEnum = ClientSideEnum.getClientSide(httpServletRequest.getHeader(HttpHeaderConst.CLIENT_SIDE));
        final String clientSide = clientSideEnum.name().toLowerCase(Locale.ROOT);

        // 当关闭同时登陆时，删除redis中旧值
        disposeAuthenticationRedisKey(baseUserPO, clientSide);

        /*
            创建令牌
         */
        BaseTokenDTO baseTokenDTO = new BaseTokenDTO();
        baseTokenDTO.setAccessToken(IdUtil.simpleUUID());
        baseTokenDTO.setRefreshToken(IdUtil.simpleUUID());
        baseTokenDTO.setUserId(baseUserPO.getId());
        baseTokenDTO.setClientType(clientSide);
        // 默认是 browser
        TokenExpires tokenExpires = TokenExpires.getTokenExpires(clientSideEnum, tokenExpiresProperties);

        // 处理过期时间和类型
        disposeToken(tokenExpires, baseTokenDTO);
        // 持久化token
        BaseTokenDTO tokenDTO = baseTokenService.save(baseTokenDTO);

        /*
            将令牌和用户基本信息存储到redis中
         */
        BaseUserDTO baseUser = BeanUtil.copyProperties(baseUserPO, BaseUserDTO.class);

        // 保存redis
        baseUserService.saveAccessToken2Redis(baseUserPO, clientSideEnum, tokenDTO.getAccessToken());

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
     * 判断是否允许同时登录多设备，当不允许时，删除redis中指定的key
     * @param baseUserPO 用户信息
     * @param clientSide 客户端类型
     */
    private void disposeAuthenticationRedisKey(BaseUserPO baseUserPO, String clientSide) {
        if (!tokenExpiresProperties.getEnableRepeatLogin()) {
            BaseTokenPO baseTokenPO = new BaseTokenPO();
            baseTokenPO.setUserId(baseUserPO.getId());
            baseTokenPO.setClientType(clientSide);
            BaseTokenDTO byExample = baseTokenService.findByExample(Example.of(baseTokenPO));
            // 令牌过期
            if (byExample.getAccessExpires().after(new Date())) {
                // 判断是否存在key，删除key
                boolean existKey = redisTool.existKey(RedisKeyProviderEnum.AUTHENTICATION, clientSide, byExample.getAccessToken());
                if (existKey) {
                    redisTool.deleteKey(RedisKeyProviderEnum.AUTHENTICATION, clientSide, byExample.getAccessToken());
                }
            }
        }
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
