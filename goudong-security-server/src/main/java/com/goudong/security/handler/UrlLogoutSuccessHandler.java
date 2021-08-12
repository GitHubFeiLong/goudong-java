package com.goudong.security.handler;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.RedisKeyEnum;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.JwtTokenUtil;
import com.goudong.commons.utils.RedisOperationsUtil;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    @Resource
    private RedisOperationsUtil redisOperationsUtil;

    /**
     * 退出登录
     * @param httpServletRequest
     * @param httpServletResponse
     * @param authentication 这个值为null可能配置出错了
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setStatus(200);
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;;charset=UTF-8");
        PrintWriter out = httpServletResponse.getWriter();
        out.write(JSON.toJSONString(Result.ofSuccess("退出成功")));

        // 获取token
        String token = httpServletRequest.getHeader(JwtTokenUtil.TOKEN_HEADER);
        if (token != null) {
            // 获取登录用户
            AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);
            // 清除用户在线token,清除用户能访问的菜单
            RedisKeyEnum[] deleteKeys = {RedisKeyEnum.OAUTH2_TOKEN_INFO, RedisKeyEnum.OAUTH2_USER_MENU};
            // 对应参数二维数组
            String[][] params = {
                    {authorityUserDTO.getUuid()},
                    {authorityUserDTO.getUuid()}
            };

            // 删除redis中的数据
            redisOperationsUtil.deleteKeys(deleteKeys, params);
        }

        out.flush();
        out.close();
    }
}
