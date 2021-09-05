package com.goudong.commons.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * 用户拦截器
 * @author msi
 * @version 1.0
 * @date 2021/9/5 16:10
 */
@Slf4j
@Component
@Deprecated
public class UserInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("拦截器");
        System.out.println("1111111111111111111");
        return true;
    }
}
