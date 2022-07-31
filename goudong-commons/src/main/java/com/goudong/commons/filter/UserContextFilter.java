package com.goudong.commons.filter;

import com.alibaba.fastjson.JSONObject;
import com.goudong.commons.constant.core.HttpHeaderConst;
import com.goudong.commons.core.context.UserContext;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.utils.core.LogUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URLDecoder;

/**
 * 类描述：
 * 给下游服务填充发起请求的用户信息
 * 注意事项：
 * 1. WebFilter注解使用虽然很简单，但是不能解决排序的问题。@WebFilter(urlPatterns = "/*", filterName = "userContextFilter")
 * 需要在启动类上添加：@ServletComponentScan(basePackageClasses = {UserContextFilter.class, BpmAuthenticationFilter.class})
 * 2. 使用 UserContextFilterConfig#userContextFilter() 能设置顺序（推荐）
 * @author msi
 * @version 1.0
 * @date 2022/1/23 10:50
 */
@Slf4j
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
        String requestUser = ((HttpServletRequest) servletRequest).getHeader(HttpHeaderConst.X_REQUEST_USER);
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