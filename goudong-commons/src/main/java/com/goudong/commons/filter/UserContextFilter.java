package com.goudong.commons.filter;

import com.alibaba.fastjson.JSONObject;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.dto.oauth2.UserContext;
import com.goudong.commons.utils.core.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 类描述：
 * 给下游服务填充发起请求的用户信息
 * @author msi
 * @version 1.0
 * @date 2022/1/23 10:50
 */
@Order
@Slf4j
@WebFilter(urlPatterns = "/*", filterName = "userContextFilter")
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
                LogUtil.debug(log, "当前请求用户信息：{}", baseUserDTO);
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