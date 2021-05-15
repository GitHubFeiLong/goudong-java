package com.goudong.security.handler;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.enumerate.ClientExceptionEnumInterface;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.pojo.Result;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 自定义未登录时：返回状态码401
 * @Author msi
 * @Date 2021-04-03 9:01
 * @Version 1.0
 */
@Component
public class UrlAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ClientExceptionEnumInterface notAuthentication = ClientExceptionEnumInterface.NOT_AUTHENTICATION;
        Result result = Result.ofFail(new BasicException.ClientException(notAuthentication));

        httpServletResponse.setStatus(notAuthentication.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
