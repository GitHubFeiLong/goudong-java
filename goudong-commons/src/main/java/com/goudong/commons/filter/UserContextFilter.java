package com.goudong.commons.filter;

import com.alibaba.fastjson.JSONObject;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.dto.oauth2.UserContext;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 类描述：
 * 请求用户拦截器
 * @author msi
 * @version 1.0
 * @date 2022/1/23 10:50
 */
@WebFilter(urlPatterns = "/*", filterName = "userContextFilter")
@Order
public class UserContextFilter implements Filter {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestUser = ((HttpServletRequest) servletRequest).getHeader(HttpHeaderConst.REQUEST_USER);
        try {
            if (requestUser != null) {
                String decodeJson = URLDecoder.decode(requestUser, "UTF-8");
                BaseUserDTO baseUserDTO = JSONObject.parseObject(decodeJson, BaseUserDTO.class);
                UserContext.set(baseUserDTO);
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            UserContext.remove();
        }
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }



}