package com.goudong.oauth2.handler;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.openfeign.UserService;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.vo.AuthorityUserVO;
import com.goudong.commons.vo.BaseToken2CreateVO;
import com.goudong.oauth2.mapper.SelfAuthorityUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * 登录成功处理器
 * @Author msi
 * @Date 2021-04-02 13:33
 * @Version 1.0
 */
@Slf4j
@Component
public class UrlAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Resource
    private SelfAuthorityUserMapper selfAuthorityUserMapper;

    // @Resource
    // private AuthorityUserUtil authorityUserUtil;

    @Resource
    private UserService userService;


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

        httpServletResponse.setCharacterEncoding("UTF-8");

        //表单输入的用户名
        String username = (String) authentication.getPrincipal();
        // 查询用户信息
        AuthorityUserDTO authorityUserDTO = selfAuthorityUserMapper.selectUserDetailByUsername(username);

        String token = JwtTokenUtil.generateBearerToken(authorityUserDTO, JwtTokenUtil.VALID_HOUR);

        // 保存token到数据库统一管理
        ArrayList<BaseToken2CreateVO> baseToken2CreateVOS = Lists.newArrayList(
                new BaseToken2CreateVO(authorityUserDTO.getId(), token)
        );
        userService.createTokens(baseToken2CreateVOS);

        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        PrintWriter out = httpServletResponse.getWriter();
        // 转成VO
        AuthorityUserVO authorityUserVO = BeanUtil.copyProperties(authorityUserDTO, AuthorityUserVO.class);
        out.write(JSON.toJSONString(Result.ofSuccess(authorityUserVO)));
        // 设置到响应头里
        httpServletResponse.setHeader(JwtTokenUtil.TOKEN_HEADER, token);

        // 将用户登录信息保存到redis中
        // authorityUserUtil.login(token, authorityUserDTO);

        out.flush();
        out.close();
    }
}
