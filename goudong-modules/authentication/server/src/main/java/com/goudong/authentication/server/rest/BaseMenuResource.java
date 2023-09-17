package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.req.BaseRoleCreateReq;
import com.goudong.authentication.server.rest.req.BaseRoleUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseMenuGetAllResp;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.authentication.server.service.manager.BaseMenuManagerService;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * REST controller for managing {@link BaseMenu}.
 */
@Api(tags = "菜单管理")
@RestController
@RequestMapping("/menu")
public class BaseMenuResource {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseMenuManagerService baseMenuManagerService;

    //~methods
    //==================================================================================================================

    @PostMapping("/base-menus")
    @ApiOperation(value = "查询所有菜单")
    public Result<BaseMenuGetAllResp> getAll(@RequestBody @Validated BaseMenuGetAllReq req) {
        return Result.ofSuccess(baseMenuManagerService.getAll(req));
    }

    @PostMapping("/base-menu")
    @ApiOperation(value = "新增菜单")
    public Result<BaseRoleDTO> create(@RequestBody @Validated BaseRoleCreateReq req) {
        return Result.ofSuccess(null);
    }

    @PutMapping("/base-menu")
    @ApiOperation(value = "修改菜单")
    public Result<BaseRoleDTO> update(@RequestBody @Validated BaseRoleUpdateReq req) {
        return Result.ofSuccess(null);
    }

    @PutMapping("/base-menu/sort_num")
    @ApiOperation(value = "修改菜单排序")
    public Result<BaseRoleDTO> changeSortNum(@RequestBody @Validated BaseRoleUpdateReq req) {
        return Result.ofSuccess(null);
    }

    @DeleteMapping("/base-menus")
    @ApiOperation(value = "批量删除菜单")
    public Result<Boolean> delete(@RequestBody @NotNull Long[] ids) {
        return Result.ofSuccess(null);
    }

}
