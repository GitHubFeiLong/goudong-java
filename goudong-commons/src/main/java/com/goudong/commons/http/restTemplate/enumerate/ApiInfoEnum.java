package com.goudong.commons.http.restTemplate.enumerate;

import lombok.Getter;
import org.springframework.http.HttpMethod;

/**
 * 枚举描述：
 * 接口枚举
 * @see ApiParameterTypeEnum
 * @author msi
 * @version 1.0
 * @date 2021/10/27 22:01
 */
@Getter
public enum ApiInfoEnum {

    CONTROLLER__METHOD("/api/user/open/user/demo", HttpMethod.GET, ApiParameterTypeEnum.NON, "测试"),
    ;
    /**
     * 接口地址
     */
    private String api;

    /**
     * 请求方式
     */
    private HttpMethod httpMethod;

    /**
     * 接口参数类型
     */
    private ApiParameterTypeEnum parameterTypeEnum;

    /**
     * 接口描述
     */
    private String apiRemark;

    ApiInfoEnum(String api, HttpMethod httpMethod, ApiParameterTypeEnum parameterTypeEnum, String apiRemark) {
        this.api = api;
        this.httpMethod = httpMethod;
        this.parameterTypeEnum = parameterTypeEnum;
        this.apiRemark = apiRemark;
    }
}
