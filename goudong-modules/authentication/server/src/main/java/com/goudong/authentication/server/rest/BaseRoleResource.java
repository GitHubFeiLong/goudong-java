package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.rest.req.BaseRoleCreate;
import com.goudong.authentication.server.rest.req.BaseRoleUpdate;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDown;
import com.goudong.authentication.server.rest.req.search.BaseRolePage;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;


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

    private final Logger log = LoggerFactory.getLogger(BaseRoleResource.class);

    @Resource
    private BaseRoleService baseRoleService;

    @PostMapping("/base-role")
    @ApiOperation(value = "新增角色")
    public Result<BaseRoleDTO> create(@RequestBody @Validated BaseRoleCreate req) {
        return Result.ofSuccess(baseRoleService.save(req));
    }

    @PutMapping("/base-role")
    @ApiOperation(value = "修改角色")
    public Result<BaseRoleDTO> update(@RequestBody @Validated BaseRoleUpdate req) {
        return Result.ofSuccess(baseRoleService.save(req));
    }

    @DeleteMapping("/base-role/{id}")
    @ApiOperation(value = "删除角色")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.ofSuccess(baseRoleService.delete(id));
    }

    @GetMapping("/base-roles")
    @ApiOperation(value = "分页角色")
    public Result<PageResult<BaseRolePage>> page(@Validated BaseRolePage req) {
        return Result.ofSuccess(baseRoleService.page(req));
    }

    @GetMapping("/base-role/drop-down")
    @ApiOperation(value = "角色下拉")
    public Result<List<BaseRoleDropDown>> dropDown(BaseRoleDropDown req) {
        return Result.ofSuccess(baseRoleService.dropDown(req));
    }
}
