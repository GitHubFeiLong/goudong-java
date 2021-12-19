package com.goudong.oauth2.handler;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.JwtTokenUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义注销成功处理器：返回状态码200
 */
@Component
public class UrlLogoutSuccessHandler implements LogoutSuccessHandler {

    // @Resource
    // private AuthorityUserUtil authorityUserUtil;

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
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;;charset=UTF-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.write(JSON.toJSONString(Result.ofSuccess("退出成功")));

        // 获取token
        String token = httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER);

        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);
        if (token != null) {
            // authorityUserUtil.logout(token, authorityUserDTO.getId());
        }

        out.flush();
        out.close();
    }
}
