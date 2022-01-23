package com.goudong.oauth2.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.exception.oauth2.AccessTokenExpiredException;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.service.BaseTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 类描述：
 * 自定义注销成功处理器：返回状态码200
 * @author msi
 * @date 2022/1/15 20:07
 * @version 1.0
 */
@Component
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    /**
     * redis 工具
     */
    private final RedisTool redisTool;

    /**
     * 令牌服务接口
     */
    private final BaseTokenService baseTokenService;

    public LogoutSuccessHandlerImpl(RedisTool redisTool, BaseTokenService baseTokenService) {
        this.redisTool = redisTool;
        this.baseTokenService = baseTokenService;
    }

    /**
     * 退出登录
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication 这个值为null可能配置出错了
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        //
        ClientSideEnum clientSideEnum = ClientSideEnum.getClientSide(httpServletRequest);
        String accessToken = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(accessToken) || !accessToken.startsWith("Bearer ")) {
            throw new AccessTokenExpiredException("退出登录失败", "用户处于离线状态，退出登录失败");
        }

        /*
            删除令牌：redis, mysql
         */
        // 删除redis
        List<RedisKeyProviderEnum> deleteKes = Lists.newArrayList(
                RedisKeyProviderEnum.AUTHENTICATION
        );
        Object[][] objects = {
                {clientSideEnum.getLowerName(), accessToken}
        };
        redisTool.deleteKeys(deleteKes, objects);

        // 删除mysql
        baseTokenService.deleteByAccessTokenAndClientType(accessToken, clientSideEnum.getLowerName());

        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        String json = new ObjectMapper().writeValueAsString(Result.ofSuccess());
        httpServletResponse.getWriter().write(json);
    }
}
