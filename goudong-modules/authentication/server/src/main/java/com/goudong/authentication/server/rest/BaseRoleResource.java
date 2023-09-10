package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.rest.req.BaseRoleChangePermissionReq;
import com.goudong.authentication.server.rest.req.BaseRoleCreateReq;
import com.goudong.authentication.server.rest.req.BaseRolePageReq;
import com.goudong.authentication.server.rest.req.BaseRoleUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseRolePermissionListResp;
import com.goudong.authentication.server.rest.resp.BaseRolePageResp;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.authentication.server.service.manager.BaseRoleManagerService;
import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.Arrays;


/**
 * <pre>
 * 新增
 * 编辑
 * 删除
 * 分页
 * 下拉
 * </pre>
 */
@Api(tags = "角色")
@RestController
@RequestMapping("/role")
public class BaseRoleResource {

    //~fields
    //==================================================================================================================
    @Resource
    private BaseRoleManagerService baseRoleManagerService;

    //~methods
    //==================================================================================================================

    @PostMapping("/page/base-roles")
    @ApiOperation(value = "分页角色")
    public Result<PageResult<BaseRolePageResp>> page(@RequestBody @Validated BaseRolePageReq req) {
        return Result.ofSuccess(baseRoleManagerService.page(req));
    }

    @PostMapping("/base-role")
    @ApiOperation(value = "新增角色")
    public Result<BaseRoleDTO> create(@RequestBody @Validated BaseRoleCreateReq req) {
        return Result.ofSuccess(baseRoleManagerService.save(req));
    }

    @PutMapping("/base-role")
    @ApiOperation(value = "修改角色")
    public Result<BaseRoleDTO> update(@RequestBody @Validated BaseRoleUpdateReq req) {
        return Result.ofSuccess(baseRoleManagerService.update(req));
    }

    @DeleteMapping("/base-roles")
    @ApiOperation(value = "批量删除角色")
    public Result<Boolean> delete(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(baseRoleManagerService.deleteByIds(Arrays.asList(ids)));
    }

    @GetMapping("/base-role/permission-list/{id}")
    @ApiOperation(value = "查询角色权限")
    public Result<BaseRolePermissionListResp> getPermissionListById(@PathVariable Long id) {
        return Result.ofSuccess(baseRoleManagerService.getPermissionListById(id));
    }

    @PostMapping("/base-role/permission-list")
    @ApiOperation(value = "角色详情(菜单权限)")
    public Result<Boolean> changePermission(@RequestBody @Validated BaseRoleChangePermissionReq req) {
        return Result.ofSuccess(baseRoleManagerService.changePermission(req));
    }



}
