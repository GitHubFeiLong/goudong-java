package com.goudong.wx.central.control.controller;

import com.goudong.core.lang.Result;
import com.goudong.wx.central.control.service.AccessTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 类描述：
 * 访问令牌
 * @author cfl
 * @version 1.0
 * @date 2023/3/14 11:54
 */
@Api(tags = "Access Token")
@RestController
@RequestMapping("/access-token")
public class AccessTokenController {

    @Resource
    private AccessTokenService accessTokenService;

    @GetMapping
    @ApiOperation("获取Access Token")
    public Result getAccessToken(@RequestParam("appId") String appId) {
        return Result.ofSuccess(accessTokenService.getAccessToken(appId));
    }

}
