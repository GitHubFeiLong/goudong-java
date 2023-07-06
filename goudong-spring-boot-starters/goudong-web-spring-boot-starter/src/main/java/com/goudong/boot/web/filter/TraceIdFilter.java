package com.goudong.boot.web.filter;

import com.goudong.boot.web.util.TraceIdUtil;
import com.goudong.core.util.StringUtil;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 类描述：
 * 拦截器，给请求日志加上追踪id
 * @author cfl
 * @version 1.0
 * @date 2023/4/18 10:13
 */
public class TraceIdFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            // 添加请求日志的全局唯一id
            String traceId = ((HttpServletRequest) request).getHeader("X-Trace-Id");
            if (StringUtil.isNotBlank(traceId)) {
                TraceIdUtil.put(traceId);
            } else {
                TraceIdUtil.put();
            }
            chain.doFilter(request, response);
        } finally {
            // 清除请求的全局日志id
            TraceIdUtil.remove();
        }
    }

}
