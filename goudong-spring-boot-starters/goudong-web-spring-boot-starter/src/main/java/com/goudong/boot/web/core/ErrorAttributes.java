package com.goudong.boot.web.core;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.boot.web.enumerate.ServerExceptionEnum;
import com.goudong.core.lang.Result;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

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

    private final HttpServletRequest request;

    public ErrorAttributes(HttpServletRequest request){
        this.request = request;
    }

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
        } else if (error == null) {
            BasicException be = ClientException.client(ClientExceptionEnum.NOT_FOUND);
            Result result = Result.ofFail(be);
            Map<String, Object> map = BeanUtil.beanToMap(result);

            // 设置响应码值
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, be.getStatus());
            return map;
        } else {
            // 其它不确定异常
            BasicException be = ServerException.serverException(ServerExceptionEnum.SERVER_ERROR, error.getMessage());
            Result result = Result.ofFail(be);
            Map<String, Object> map = BeanUtil.beanToMap(result);

            // 设置响应码值
            request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, be.getStatus());
            return map;
        }

        // 使用默认的错误
        // return super.getErrorAttributes(webRequest, options);
    }

}