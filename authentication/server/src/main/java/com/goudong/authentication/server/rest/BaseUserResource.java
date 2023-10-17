package com.goudong.authentication.server.rest;

import com.goudong.authentication.common.core.Token;
import com.goudong.authentication.common.core.UserDetail;
import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserPageReq;
import com.goudong.authentication.server.rest.req.BaseUserSimpleCreateReq;
import com.goudong.authentication.server.rest.req.BaseUserSimpleUpdateReq;
import com.goudong.authentication.server.rest.req.RefreshToken;
import com.goudong.authentication.server.rest.resp.BaseUserPageResp;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
import com.goudong.authentication.server.service.manager.BaseUserManagerService;
import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;


/**
 * REST controller for managing {@link BaseUser}.
 * TODO 创建，删除，修改，分页，
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
@Slf4j
public class BaseUserResource {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseUserManagerService baseUserManagerService;

    //~methods
    //==================================================================================================================
    @PostMapping("/login")
    @ApiOperation(value = "登录(password)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", required = false),
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    public Result<Token> login() {
        return Result.ofSuccess(new Token());
    }

    @PostMapping("/refresh-token")
    @ApiOperation(value = "刷新token")
    public Result<Token> refreshToken(@RequestBody RefreshToken token) {
        return Result.ofSuccess(baseUserManagerService.refreshToken(token));
    }

    @PutMapping("/logout")
    @ApiOperation(value = "注销")
    public Result<Object> logout() {
        return Result.ofSuccess();
    }

    @GetMapping("/base-user/detail/{token}")
    @ApiOperation(value = "查询用户信息(token)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "token", value = "令牌", required = true),
    })
    public Result<UserDetail> getUserDetailByToken(@PathVariable String token) {
        return Result.ofSuccess(baseUserManagerService.getUserDetailByToken(token));
    }

    @PostMapping("/page/base-users")
    @ApiOperation(value = "分页用户")
    public Result<PageResult<BaseUserPageResp>> page(@RequestBody @Validated BaseUserPageReq req) {
        return Result.ofSuccess(baseUserManagerService.page(req));
    }
    @PostMapping("/base-user/simple-create")
    @ApiOperation(value = "简单新增用户")
    public Result<BaseUserDTO> simpleCreateUser(@RequestBody @Validated BaseUserSimpleCreateReq req) {
        return Result.ofSuccess(baseUserManagerService.simpleCreateUser(req));
    }

    @PutMapping("/base-user/simple-update")
    @ApiOperation(value = "修改用户")
    public Result<BaseUserDTO> update(@RequestBody @Validated BaseUserSimpleUpdateReq req) {
        return Result.ofSuccess(baseUserManagerService.simpleUpdateUser(req));
    }

    @PutMapping("/base-user/reset-password/{userId}")
    @ApiOperation(value = "重置密码")
    public Result<Boolean> resetPassword(@PathVariable Long userId) {
        return Result.ofSuccess(baseUserManagerService.resetPassword(userId));
    }

    @PutMapping("/base-user/change-enabled/{userId}")
    @ApiOperation(value = "修改激活状态")
    public Result<Boolean> changeEnabled(@PathVariable Long userId) {
        return Result.ofSuccess(baseUserManagerService.changeEnabled(userId));
    }

    @PutMapping("/base-user/change-locked/{userId}")
    @ApiOperation(value = "修改锁定状态")
    public Result<Boolean> changeLocked(@PathVariable Long userId) {
        return Result.ofSuccess(baseUserManagerService.changeLocked(userId));
    }

    @DeleteMapping("/base-users")
    @ApiOperation(value = "批量删除用户")
    public Result<Boolean> deleteByIds(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(baseUserManagerService.deleteByIds(Arrays.asList(ids)));
    }

    // TODO 导出用户
}
