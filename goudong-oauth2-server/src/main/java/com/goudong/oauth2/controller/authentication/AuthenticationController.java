package com.goudong.oauth2.controller.authentication;

import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.frame.core.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 类描述：
 * 认证相关的接口
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "登录")
@Slf4j
@Validated
@RestController
@RequestMapping("/authentication")
public class AuthenticationController {

    /**
     * 登录接口
     * @param username
     * @param password
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "登录(password)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    @Whitelist("登录接口")
    public Result login (String username, String password) {
        return Result.ofSuccess();
    }

    /**
     * 注销接口
     * @return
     */
    @PutMapping("/logout")
    @ApiOperation(value = "注销")
    @Whitelist("注销登录接口")
    public Result logout () {
        return Result.ofSuccess();
    }

    /**
     * 获取当前用户
     * @return
     */
    @GetMapping("/current-user-info")
    @ApiOperation("获取登录用户信息")
    public Result currentUser() {
        return Result.ofSuccess(SecurityContextHolder.getContext().getAuthentication());
    }


}
