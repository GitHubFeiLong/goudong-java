package com.goudong.oauth2.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.openfeign.GoudongUserServerService;
import com.goudong.oauth2.core.AuthenticationImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 认证成功处理器
 * @author msi
 * @date 2022/1/15 21:46
 * @version 1.0
 */
@Slf4j
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    // @Resource
    // private SelfAuthorityUserMapper selfAuthorityUserMapper;

    // @Resource
    // private AuthorityUserUtil authorityUserUtil;

    @Resource
    private GoudongUserServerService userService;


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
        AuthenticationImpl authenticationImpl = (AuthenticationImpl) authentication;

        // 将其设置到上下文中
        SecurityContextHolder.getContext().setAuthentication(authenticationImpl);

        // authenticationImpl.setRoles(null);
        httpServletResponse.setCharacterEncoding("UTF-8");

        //表单输入的用户名
        String username = (String) authentication.getPrincipal();
        //
        //
        // String token = JwtTokenUtil.generateBearerToken(authorityUserDTO, JwtTokenUtil.VALID_HOUR);
        //
        // // 保存token到数据库统一管理
        // ArrayList<BaseToken2CreateVO> baseToken2CreateVOS = Lists.newArrayList(
        //         new BaseToken2CreateVO(authorityUserDTO.getId(), token)
        // );
        // userService.createTokens(baseToken2CreateVOS);
        //
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String json = new ObjectMapper().writeValueAsString(authentication);
        httpServletResponse.getWriter().write(json);
        // // 转成VO
        // AuthorityUserVO authorityUserVO = BeanUtil.copyProperties(authorityUserDTO, AuthorityUserVO.class);
        // out.write(JSON.toJSONString(Result.ofSuccess(authorityUserVO)));
        // // 设置到响应头里
        // httpServletResponse.setHeader(JwtTokenUtil.TOKEN_HEADER, token);

        // 将用户登录信息保存到redis中
        // authorityUserUtil.login(token, authorityUserDTO);
    }
}
