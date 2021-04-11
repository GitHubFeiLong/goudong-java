package com.zuopei.oauth2.service;

import com.zuopei.commons.pojo.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * 鉴权服务接口
 * @Author msi
 * @Date 2021-04-09 9:07
 * @Version 1.0
 */
public interface AuthenticationService {

    /**
     * 鉴权
     * @param request
     * @param httpPath
     * @param httpMethod
     * @return
     */
    Result authentication (HttpServletRequest request, String httpPath, String httpMethod);
}
