package com.goudong.commons.http.restTemplate.enumerate;

/**
 * 枚举描述：
 * 接口参数类型枚举
 * @author msi
 * @version 1.0
 * @date 2021/10/27 22:05
 */
public enum ApiParameterTypeEnum {
    /**
     * 无参数
     */
    NON,
    /**
     * 路径参数
     */
    PATH_VARIABLE,

    /**
     * 键值对
     */
    REQUEST_PARAM,

    /**
     * json
     */
    REQUEST_BODY
    ;
}
