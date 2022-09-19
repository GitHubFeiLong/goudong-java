package com.goudong.user.controller.role;

import com.goudong.commons.annotation.core.HideMenu;
import com.goudong.commons.dto.core.BasePageResult;
import com.goudong.commons.framework.core.Result;
import com.goudong.user.dto.AddRoleReq;
import com.goudong.user.dto.BaseRole2QueryPageDTO;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.dto.ModifyRoleReq;
import com.goudong.user.service.BaseRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：
 * 角色控制层
 * @author cfl
 * @version 1.0
 * @date 2022/8/23 20:53
 */
@Api(tags = "角色相关接口")
@Slf4j
@RestController
@RequestMapping("/base-role")
@RequiredArgsConstructor
public class BaseRoleController {
    //~fields
    //==================================================================================================================
    private final BaseRoleService baseRoleService;
    //~methods
    //==================================================================================================================
    @GetMapping("/page")
    @ApiOperation(value = "角色分页查询")
    public Result<BasePageResult<BaseRoleDTO>> page (BaseRole2QueryPageDTO page){
        return Result.ofSuccess(baseRoleService.page(page));
    }

    @HideMenu("哈哈哈哈")
    @GetMapping("/{id}")
    @ApiOperation(value = "根据id查询角色信息")
    public Result<BaseRoleDTO> getById (@PathVariable("id") Long id){
        return Result.ofSuccess(baseRoleService.getById(id));
    }

    @PostMapping
    @ApiOperation(value = "新增角色")
    public Result<BaseRoleDTO> addRole (@RequestBody @Validated AddRoleReq req){
        return Result.ofSuccess(baseRoleService.addRole(req));
    }

    @PutMapping
    @ApiOperation(value = "修改角色")
    public Result<BaseRoleDTO> modifyRole (@RequestBody @Validated ModifyRoleReq req){
        return Result.ofSuccess(baseRoleService.modifyRole(req));
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除角色")
    public Result<BaseRoleDTO> removeRole (@PathVariable @Min(value = 100) Long id){
        return Result.ofSuccess(baseRoleService.removeRole(id));
    }

    @PostMapping("/permissions/{id}")
    @ApiOperation(value = "修改权限")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "角色id"),
            @ApiImplicitParam(name = "menuIds", value = "菜单id"),
    })
    public Result<BaseRoleDTO> updatePermissions(@PathVariable("id")@Min(value = 100) Long id,
                                                 @RequestBody @NotNull List<Long> menuIds) {
        return Result.ofSuccess(baseRoleService.updatePermissions(id, menuIds));
    }


}
