package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.domain.BaseUser;
import com.goudong.authentication.server.rest.req.BaseUserCreate;
import com.goudong.authentication.server.rest.req.BaseUserUpdate;
import com.goudong.authentication.server.rest.req.search.BaseUserDropDown;
import com.goudong.authentication.server.rest.req.search.BaseUserPage;
import com.goudong.authentication.server.service.BaseUserService;
import com.goudong.authentication.server.service.dto.BaseUserDTO;
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
import java.util.List;


/**
 * REST controller for managing {@link BaseUser}.
 * TODO 创建，删除，修改，分页，
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户")
@Slf4j
public class BaseUserResource {

    @Resource
    private BaseUserService baseUserService;

    @PostMapping("/login")
    @ApiOperation(value = "登录(password)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "应用id", required = false),
            @ApiImplicitParam(name = "username", value = "用户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true),
    })
    public Result login () {
        return Result.ofSuccess();
    }

    /**
     * 注销接口
     * @return
     */
    @PutMapping("/logout")
    @ApiOperation(value = "注销")
    public Result logout () {
        return Result.ofSuccess();
    }

    @PostMapping("/base-user")
    @ApiOperation(value = "新增用户")
    public Result<BaseUserDTO> create(@RequestBody @Validated BaseUserCreate req) {
        return Result.ofSuccess(baseUserService.save(req));
    }

    // @GetMapping("/base-user/{id}")
    @ApiOperation(value = "查询用户详情", hidden = true)
    @Deprecated
    public Result<BaseUserDTO> getById(@PathVariable Long id) {
        return Result.ofSuccess(baseUserService.getById(id));
    }

    @PutMapping("/base-user")
    @ApiOperation(value = "修改用户")
    public Result<BaseUserDTO> update(@RequestBody @Validated BaseUserUpdate req) {
        return Result.ofSuccess(baseUserService.save(req));
    }

    @DeleteMapping("/base-user/{id}")
    @ApiOperation(value = "删除用户")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ofSuccess(baseUserService.delete(id));
    }

    @GetMapping("/base-users")
    @ApiOperation(value = "分页用户")
    public Result<PageResult<BaseUserPage>> page(@Validated BaseUserPage req) {
        return Result.ofSuccess(baseUserService.page(req));
    }

    @GetMapping("/base-user/drop-down")
    @ApiOperation(value = "用户下拉")
    public Result<List<BaseUserDropDown>> dropDown(BaseUserDropDown req) {
        return Result.ofSuccess(baseUserService.dropDown(req));
    }
}
