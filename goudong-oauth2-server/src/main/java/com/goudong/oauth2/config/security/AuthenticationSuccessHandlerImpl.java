package com.goudong.oauth2.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.frame.redis.RedisTool;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.oauth2.core.LoginInfo;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.properties.TokenExpiresProperties;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;

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
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException {
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");

        // 转换成自定义的Authentication对象
        BaseUserPO baseUserPO = (BaseUserPO) authentication;

        // 持久化token到Mysql
        BaseTokenDTO tokenDTO = baseTokenService.login(baseUserPO.getId());

        // 将认证信息存储到redis中
        BaseUserDTO baseUser = BeanUtil.copyProperties(baseUserPO, BaseUserDTO.class);
        baseUserService.saveAccessToken2Redis(baseUserPO, tokenDTO.getAccessToken());

        // 响应令牌和用户信息
        LoginInfo loginInfo = BeanUtil.copyProperties(tokenDTO, LoginInfo.class);
        loginInfo.setUser(baseUser);
        String json = new ObjectMapper().setDateFormat(new SimpleDateFormat(DateConst.DATE_TIME_FORMATTER))
                .writeValueAsString(Result.ofSuccess(loginInfo));

        httpServletResponse.getWriter().write(json);
    }

}
