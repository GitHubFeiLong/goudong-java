package com.goudong.user.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 * 扩充全局异常
 * @author msi
 * @version 1.0
 * @date 2022/1/15 15:25
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends com.goudong.boot.web.core.GlobalExceptionHandler{
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    public GlobalExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

}
