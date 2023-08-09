package com.goudong.boot.web.core;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.web.servlet.error.AbstractErrorController;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;

import javax.annotation.Resource;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * 自定义 /error 映射
 * 例如URL地址错误，拦截器发生异常。这种异常处理需要 异常处理类通过实现 ErrorController
 * @Author msi
 * @Date 2021-05-25 10:53
 * @Version 1.0
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class ErrorController extends AbstractErrorController {

    @Resource
    private ErrorAttributesService errorAttributesService;

    public ErrorController(org.springframework.boot.web.servlet.error.ErrorAttributes errorAttributes) {
        super(errorAttributes);
    }

    /**
     * mvc 错误时，跳转到/error请求，并自定义返回状态码和对应的json数据
     * @param request
     * @return
     */
    @RequestMapping
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request, HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        Map<String, Object> body = errorAttributesService.getErrorAttributes(new ServletWebRequest(request), ErrorAttributeOptions.defaults());

        int code = (int)request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus status = HttpStatus.resolve(code);
        return new ResponseEntity<>(body, status);
    }

    /**
     * @deprecated
     */
    @Override
    public String getErrorPath() {
        return null;
    }
}
