package com.goudong.user.controller.role;

import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.Result;
import com.goudong.core.util.AssertUtil;
import com.goudong.user.dto.AddRoleReq;
import com.goudong.user.dto.BaseRole2QueryPageDTO;
import com.goudong.user.dto.BaseRoleDTO;
import com.goudong.user.dto.ModifyRoleReq;
import com.goudong.user.exception.RoleException;
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
import javax.validation.constraints.NotEmpty;
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
    public Result<PageResult<BaseRoleDTO>> page (BaseRole2QueryPageDTO page){
        return Result.ofSuccess(baseRoleService.page(page));
    }

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
        // 参数校验，预置角色不能删除
        return Result.ofSuccess(baseRoleService.removeRole(id));
    }

    @DeleteMapping("/ids")
    @ApiOperation(value = "批量删除角色", notes = "只能删除没有用户的角色")
    @ApiImplicitParam(name = "ids", value = "role ids", required = true)
    public Result<Boolean> deleteUserById (@RequestParam(name = "ids")@NotNull @NotEmpty List<Long> ids){
        // 参数校验，预置角色不能删除
        boolean present = ids.stream().filter(f -> f < 100).findFirst().isPresent();
        AssertUtil.isFalse(present, () -> RoleException.client("角色删除失败"));
        return Result.ofSuccess(baseRoleService.removeRoles(ids));
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

    @GetMapping("/page/name")
    @ApiOperation(value = "角色名模糊分页", notes = "根据角色名称进行分页查询")
    public Result<PageResult<BaseRoleDTO>> pageRoleName (BaseRole2QueryPageDTO page){
        return Result.ofSuccess(baseRoleService.pageRoleName(page));
    }
}
