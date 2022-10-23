package com.goudong.oauth2.config.security;

import com.goudong.commons.constant.core.DateConst;
import com.goudong.commons.exception.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.framework.core.Result;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 未经身份验证
 *
 * @Author msi
 * @Date 2021-04-03 9:01
 * @Version 1.0
 */
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException {
        ClientExceptionEnum notAuthentication = ClientExceptionEnum.UNAUTHORIZED;
        Result result = Result.ofFail(new ClientException(notAuthentication));

        httpServletResponse.setStatus(notAuthentication.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.setHeader(HttpHeaders.WWW_AUTHENTICATE, HttpHeaders.WWW_AUTHENTICATE);
        String json = new Jackson2ObjectMapperBuilder()
                .simpleDateFormat(DateConst.DATE_TIME_FORMATTER)
                .build()
                .writeValueAsString(Result.ofSuccess(result));;
        httpServletResponse.getWriter().write(json);
    }
}
