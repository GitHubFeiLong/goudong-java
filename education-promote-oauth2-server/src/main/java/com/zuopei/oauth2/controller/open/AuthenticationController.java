package com.zuopei.oauth2.controller.open;

import com.zuopei.commons.pojo.Result;
import com.zuopei.oauth2.service.AuthenticationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 提供网关鉴权接口
 * @Author msi
 * @Date 2021-04-09 9:00
 * @Version 1.0
 */
@Api(value = "开放接口", tags = "提供其他服务")
@Slf4j
@RestController
@RequestMapping("/api/oauth2/open")
public class AuthenticationController {

    @Resource
    private AuthenticationService authenticationService;

    @ApiOperation(value = "提供鉴权")
    @GetMapping("/authentication")
    public Result authentication (HttpServletRequest request, String httpPath, String httpMethod) {
        log.info("进入权限判断接口");

        return authenticationService.authentication(request, httpPath, httpMethod);
    }
}
