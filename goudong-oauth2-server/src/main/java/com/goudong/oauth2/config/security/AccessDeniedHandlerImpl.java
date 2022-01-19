package com.goudong.oauth2.config.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.frame.core.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 自定义权限不足处理器：返回状态码403
 *
 * @author msi
 * @date 2022/1/15 20:00
 * @version 1.0
 */
@Slf4j
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    /**
     * 请求被拒绝处理方法
     * @param httpServletRequest
     * @param httpServletResponse
     * @param e 访问拒绝异常对象
     * @throws IOException
     */
    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        log.error("权限不足");
        ClientExceptionEnum notAuthorization = ClientExceptionEnum.NOT_AUTHORIZATION;
        Result result = Result.ofFail(notAuthorization);

        httpServletResponse.setStatus(notAuthorization.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        String json = new ObjectMapper().writeValueAsString(result);
        httpServletResponse.getWriter().write(json);
    }
}
