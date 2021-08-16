package com.goudong.commons.enumerate;

import com.goudong.commons.constant.CommonConst;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

/**
 * 枚举描述：
 * 定义接口的注解及其请求方式, 先定义常用的几种请求方式，后期根据需求增加其它（如PATCH, TRACE等）
 * @Author msi
 * @Date 2021-08-14 9:46
 * @Version 1.0
 */
@Getter
public enum RequestMappingEnum {

    /**
     * 通用，所以请求方式多种
     */
    REQUEST_MAPPING(RequestMapping.class, CommonConst.ASTERISK),
    /**
     * get请求
     */
    GET_MAPPING(GetMapping.class, HttpMethod.GET.name()),
    /**
     * post请求
     */
    POST_MAPPING(PostMapping.class, HttpMethod.POST.name()),
    /**
     * put请求
     */
    PUT_MAPPING(PutMapping.class, HttpMethod.PUT.name()),
    /**
     * delete请求
     */
    DELETE_MAPPING(DeleteMapping.class, HttpMethod.DELETE.name()),

    /**
     * patch请求
     */
    PATCH_MAPPING(PatchMapping.class, HttpMethod.PATCH.name())
    ;
    /**
     * api接口的注解（@RequestMapping）
     */
    private Class mapping;
    /**
     * 请求方式（put，post，get...）
     * 为了方便 @RequestMapping注解多种请求方式，所以就直接定义成string类型，*表示所有请求方式都可以
     */
    private String httpMethod;

    RequestMappingEnum(Class mapping, String httpMethod){
        this.mapping = mapping;
        this.httpMethod = httpMethod;
    }
}
