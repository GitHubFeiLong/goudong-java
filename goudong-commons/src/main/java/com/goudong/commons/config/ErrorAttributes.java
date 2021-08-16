package com.goudong.commons.config;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.exception.BasicException;
import com.goudong.commons.pojo.Result;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 自定义异常逻辑，返回自定义格式的json错误信息
 * @Author msi
 * @Date 2021-05-25 9:53
 * @Version 1.0
 */
@Component
public class ErrorAttributes extends DefaultErrorAttributes {
    @Resource
    private HttpServletRequest request;

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Throwable error = super.getError(webRequest);
        if (error instanceof BasicException) {
            BasicException be = (BasicException) error;
            Result result = Result.ofFail(be);
            Map<String, Object> map = BeanUtil.beanToMap(result);

            // 设置响应码值
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, be.getStatus());
            return map;
        } else if (error instanceof NoHandlerFoundException) { // 静态资源404
            NoHandlerFoundException exception = (NoHandlerFoundException) error;
            Result result = Result.ofFailByNotFound(exception.getRequestURL());
            Map<String, Object> map = BeanUtil.beanToMap(result);

            // 设置响应码值
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, HttpStatus.NOT_FOUND.value());
            return map;
        }

        return super.getErrorAttributes(webRequest, options);
    }

}
