package com.goudong.commons.enumerate.core;

import lombok.Getter;
import org.springframework.web.bind.annotation.*;

/**
 * 类描述：
 * 定义接口的注解及其请求方式, 先定义常用的几种请求方式，后期根据需求增加其它（如PATCH, TRACE等）
 * @author msi
 * @date 2022/1/8 16:28
 * @version 1.0
 */
@Getter
public enum RequestMappingEnum {

    /**
     * 通用，所以请求方式多种
     * 注意：当更新请求方式时，需要添加
     */
    REQUEST_MAPPING(RequestMapping.class, new String[]{"GET", "POST", "PUT","DELETE", "PATCH"}),
    /**
     * get请求
     */
    GET_MAPPING(GetMapping.class, new String[]{"GET"}),
    /**
     * post请求
     */
    POST_MAPPING(PostMapping.class, new String[]{"POST"}),
    /**
     * put请求
     */
    PUT_MAPPING(PutMapping.class, new String[]{"PUT"}),
    /**
     * delete请求
     */
    DELETE_MAPPING(DeleteMapping.class, new String[]{"DELETE"}),

    /**
     * patch请求
     */
    PATCH_MAPPING(PatchMapping.class, new String[]{"PATCH"}),

    ;

    public boolean validMethod() {

    }

    /**
     * api接口的注解（@RequestMapping）
     */
    private Class mapping;

    /**
     * 请求方法
     * mapping注解对应能接受的请求方法
     */
    private String[] httpMethod;

    RequestMappingEnum(Class mapping, String[] httpMethod){
        this.mapping = mapping;
        this.httpMethod = httpMethod;
    }
}
