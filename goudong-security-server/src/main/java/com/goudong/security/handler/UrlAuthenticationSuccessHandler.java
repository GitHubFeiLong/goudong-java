package com.goudong.security.handler;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.entity.AuthorityUserDO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.security.dao.AuthorityUserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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
    private AuthorityUserDao authorityUserDao;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {

        httpServletResponse.setCharacterEncoding("UTF-8");

        //表单输入的用户名
        String username = (String) authentication.getPrincipal();
        // 查询用户信息
        AuthorityUserDO authorityUserDO = authorityUserDao.selectUserDetailByUsername(username);

        // 短期有效
        String shortToken = JwtTokenUtil.generateToken(authorityUserDO, JwtTokenUtil.VALID_SHORT_TERM_HOUR);
        // 长期有效
        String longToken = JwtTokenUtil.generateToken(authorityUserDO, JwtTokenUtil.VALID_LONG_TERM_HOUR);

        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("text/html;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.ofSuccess(authorityUserDO)));
        // 设置到响应头里
        httpServletResponse.setHeader(JwtTokenUtil.TOKEN, JwtTokenUtil.TOKEN_PREFIX + shortToken);
        httpServletResponse.setHeader(JwtTokenUtil.REFRESH_TOKEN, JwtTokenUtil.TOKEN_PREFIX + longToken);
    }
}
