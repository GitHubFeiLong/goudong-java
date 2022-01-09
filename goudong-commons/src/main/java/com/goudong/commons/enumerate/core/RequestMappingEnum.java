package com.goudong.commons.enumerate.core;

import com.google.common.collect.Lists;
import com.goudong.commons.constant.core.HttpMethodConst;
import lombok.Getter;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    REQUEST_MAPPING(RequestMapping.class, HttpMethodConst.ALL_HTTP_METHOD),
    /**
     * get请求
     */
    GET_MAPPING(GetMapping.class, Lists.newArrayList(HttpMethod.GET.name())),
    /**
     * post请求
     */
    POST_MAPPING(PostMapping.class, Lists.newArrayList(HttpMethod.POST.name())),
    /**
     * put请求
     */
    PUT_MAPPING(PutMapping.class, Lists.newArrayList(HttpMethod.PUT.name())),
    /**
     * delete请求
     */
    DELETE_MAPPING(DeleteMapping.class, Lists.newArrayList(HttpMethod.DELETE.name())),

    /**
     * patch请求
     */
    PATCH_MAPPING(PatchMapping.class, Lists.newArrayList(HttpMethod.PATCH.name())),

    ;
    /**
     * api接口的注解（@RequestMapping）
     */
    private Class mapping;

    /**
     * 请求方法
     * mapping注解对应能接受的请求方法
     */
    private List<String> methods;

    RequestMappingEnum(Class mapping, List<String> methods){
        this.mapping = mapping;
        this.methods = methods;
    }
}
