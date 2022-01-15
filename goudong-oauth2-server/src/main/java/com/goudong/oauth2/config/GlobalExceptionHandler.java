package com.goudong.oauth2.config;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/15 15:25
 */
@Slf4j
// @RestControllerAdvice
public class GlobalExceptionHandler extends com.goudong.commons.exception.GlobalExceptionHandler{

    public GlobalExceptionHandler(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
}