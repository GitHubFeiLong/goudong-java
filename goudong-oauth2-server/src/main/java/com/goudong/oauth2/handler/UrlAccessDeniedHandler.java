package com.goudong.oauth2.handler;

import com.alibaba.fastjson.JSON;
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
 * @author Andon
 * @date 2019/3/20
 * <p>
 * 自定义权限不足处理器：返回状态码403
 */
@Slf4j
@Component
public class UrlAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        log.error("权限不足");
        ClientExceptionEnum notAuthorization = ClientExceptionEnum.NOT_AUTHORIZATION;
        Result result = Result.ofFail(notAuthorization);

        httpServletResponse.setStatus(notAuthorization.getStatus());
        httpServletResponse.setCharacterEncoding("UTF-8");
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }
}
