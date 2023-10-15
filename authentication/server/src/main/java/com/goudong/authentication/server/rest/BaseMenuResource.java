package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.rest.req.BaseMenuChangeSortNumReq;
import com.goudong.authentication.server.rest.req.BaseMenuCreateReq;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.req.BaseMenuUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseMenuGetAllResp;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.manager.BaseMenuManagerService;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
    public Result<BaseMenuDTO> save(@RequestBody @Validated BaseMenuCreateReq req) {
        return Result.ofSuccess(baseMenuManagerService.save(req));
    }

    @PutMapping("/base-menu")
    @ApiOperation(value = "修改菜单")
    public Result<BaseMenuDTO> update(@RequestBody @Validated BaseMenuUpdateReq req) {
        return Result.ofSuccess(baseMenuManagerService.update(req));
    }

    @PutMapping("/base-menu/sort-num")
    @ApiOperation(value = "修改菜单排序")
    public Result<Boolean> changeSortNum(@RequestBody @Validated BaseMenuChangeSortNumReq req) {
        return Result.ofSuccess(baseMenuManagerService.changeSortNum(req));
    }

    @DeleteMapping("/base-menu/{id}")
    @ApiOperation(value = "删除菜单", notes = "如果是父节点，那么就会删除它及它下面的所有子节点")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ofSuccess(baseMenuManagerService.deleteById(id));
    }

    // TODO 权限校验接口
    // @PostMapping("/checkPermission")

}
