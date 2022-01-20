package com.goudong.oauth2.config.security;

import cn.hutool.core.util.IdUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.frame.core.Result;
import com.goudong.oauth2.core.AuthenticationImpl;
import com.goudong.oauth2.core.TokenExpires;
import com.goudong.oauth2.dto.BaseTokenDTO;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

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
     * objectMapper
     */
    private final ObjectMapper objectMapper;

    public AuthenticationSuccessHandlerImpl(BaseTokenService baseTokenService,
                                            TokenExpiresProperties tokenExpiresProperties,
                                            ObjectMapper objectMapper) {
        this.baseTokenService = baseTokenService;
        this.tokenExpiresProperties = tokenExpiresProperties;
        this.objectMapper = objectMapper;
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
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        AuthenticationImpl authenticationImpl = (AuthenticationImpl) authentication;
        /*
            创建令牌
         */

        BaseTokenDTO baseTokenDTO = new BaseTokenDTO();
        baseTokenDTO.setAccessToken(IdUtil.simpleUUID());
        baseTokenDTO.setRefreshToken(IdUtil.simpleUUID());
        baseTokenDTO.setUserId(authenticationImpl.getId());

        disposeToken(httpServletRequest, baseTokenDTO);

        // 持久化token
        BaseTokenDTO tokenDTO = baseTokenService.save(baseTokenDTO);
        // redis存一份

        //表单输入的用户名
        String username = (String) authentication.getPrincipal();


        String json = objectMapper.writeValueAsString(Result.ofSuccess(tokenDTO));
        httpServletResponse.getWriter().write(json);
    }

    /**
     * 处理生成token
     * @param httpServletRequest
     * @param baseTokenDTO
     */
    private void disposeToken(HttpServletRequest httpServletRequest, BaseTokenDTO baseTokenDTO) {
        LocalDateTime now = LocalDateTime.now();
        // app
        if (Objects.equals(httpServletRequest.getHeader(""), ClientSideEnum.APP.getHeaderValue())) {
            TokenExpires app = tokenExpiresProperties.getApp();
            baseTokenDTO.setAccessExpires(
                    Date.from(
                            now.plusSeconds(app.getAccessTimeUnit().toSeconds(app.getAccess()))
                                    .atZone( ZoneId.systemDefault()).toInstant()
                    )
            );
            baseTokenDTO.setRefreshExpires(
                    Date.from(
                            now.plusSeconds(app.getRefreshTimeUnit().toSeconds(app.getRefresh()))
                                    .atZone( ZoneId.systemDefault()).toInstant()
                    )
            );

            baseTokenDTO.setClientType(ClientSideEnum.APP.name());
        } else {
            // 默认是 browser
            TokenExpires browser = tokenExpiresProperties.getBrowser();
            baseTokenDTO.setAccessExpires(
                    Date.from(
                            now.plusSeconds(browser.getAccessTimeUnit().toSeconds(browser.getAccess()))
                                    .atZone( ZoneId.systemDefault()).toInstant()
                    )
            );
            baseTokenDTO.setRefreshExpires(
                    Date.from(
                            now.plusSeconds(browser.getRefreshTimeUnit().toSeconds(browser.getRefresh()))
                                    .atZone( ZoneId.systemDefault()).toInstant()
                    )
            );
            baseTokenDTO.setClientType(ClientSideEnum.BROWSER.name());
        }
    }
}
