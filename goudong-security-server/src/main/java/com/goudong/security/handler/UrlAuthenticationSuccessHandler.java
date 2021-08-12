package com.goudong.security.handler;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.pojo.UserHashRedis;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import com.goudong.commons.vo.AuthorityUserVO;
import com.goudong.security.dao.SelfAuthorityUserDao;
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
import java.util.List;
import java.util.Optional;

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
    private SelfAuthorityUserDao selfAuthorityUserDao;

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

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
        AuthorityUserDTO authorityUserDTO = selfAuthorityUserDao.selectUserDetailByUsername(username);

        String token = JwtTokenUtil.generateToken(authorityUserDTO, JwtTokenUtil.VALID_HOUR);
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        PrintWriter out = httpServletResponse.getWriter();
        // 转成VO
        AuthorityUserVO authorityUserVO = BeanUtil.copyProperties(authorityUserDTO, AuthorityUserVO.class);
        out.write(JSON.toJSONString(Result.ofSuccess(authorityUserVO)));
        // 设置到响应头里
        httpServletResponse.setHeader(JwtTokenUtil.TOKEN_HEADER, token);

        // 添加信息到redis中
        redisOperationsUtil.setStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, token, authorityUserDTO.getUuid());
        // 为了登陆后方便判断用户是否能访问某个url,事先将能访问的菜单url放入redis中
        List<AuthorityMenuDTO> authorityMenuDTOS = authorityUserDTO.getAuthorityMenuDTOS();
        redisOperationsUtil.setListValue(RedisKeyEnum.OAUTH2_USER_MENU, authorityMenuDTOS, authorityUserDTO.getUuid());

        out.flush();
        out.close();
    }
}
