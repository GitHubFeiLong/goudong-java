package com.goudong.oauth2.controller.oauth;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.po.AuthorityRolePO;
import com.goudong.commons.pojo.PageParam;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.vo.AuthorityRole2InsertVO;
import com.goudong.commons.vo.AuthorityRole2UpdateVO;
import com.goudong.commons.vo.AuthorityRoleVO;
import com.goudong.oauth2.service.AuthorityRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：
 * 角色控制器
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "角色")
@Slf4j
@Validated
@RestController
@RequestMapping("/role")
public class AuthorityRoleController {

    private AuthorityRoleService authorityRoleService;

    @Autowired
    public void setAuthorityRoleService(AuthorityRoleService authorityRoleService) {
        this.authorityRoleService = authorityRoleService;
    }

    /**
     * 批量新增角色
     * @param insertVOList
     * @return
     */
    @PostMapping("/roles")
    @ApiOperation(value = "新增多条角色")
    public Result<List<AuthorityRoleVO>> addRoles (@RequestBody @Valid List<AuthorityRole2InsertVO> insertVOList) {
        List<AuthorityRoleDTO> authorityRoleDTOS = BeanUtil.copyList(insertVOList, AuthorityRoleDTO.class);
        authorityRoleDTOS = authorityRoleService.addRoles(authorityRoleDTOS);

        return Result.ofSuccess(BeanUtil.copyList(authorityRoleDTOS, AuthorityRoleVO.class));
    }

    /**
     * 批量新增角色
     * @param insertVO
     * @return
     */
    @PostMapping
    @ApiOperation(value = "新增单条角色")
    public Result<AuthorityRoleVO> addRoles (@RequestBody @Validated AuthorityRole2InsertVO insertVO) {
        AuthorityRoleDTO authorityRoleDTO = BeanUtil.copyProperties(insertVO, AuthorityRoleDTO.class);
        authorityRoleDTO = authorityRoleService.addRole(authorityRoleDTO);

        return Result.ofSuccess(BeanUtil.copyProperties(authorityRoleDTO, AuthorityRoleVO.class));
    }

    @PutMapping
    @ApiOperation("修改角色")
    public Result<AuthorityRoleVO> updateRole (@RequestBody @Validated AuthorityRole2UpdateVO updateVO) {
        AuthorityRoleDTO authorityRoleDTO = BeanUtil.copyProperties(updateVO, AuthorityRoleDTO.class);
        authorityRoleDTO = authorityRoleService.updateRole(authorityRoleDTO);

        return Result.ofSuccess(BeanUtil.copyProperties(authorityRoleDTO, AuthorityRoleVO.class));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除角色")
    public Result<AuthorityRoleVO> deleteRole (@PathVariable("id")Long id) {
        AuthorityRoleDTO authorityRoleDTO = authorityRoleService.deleteRole(id);

        return Result.ofSuccess(BeanUtil.copyProperties(authorityRoleDTO, AuthorityRoleVO.class));
    }

    @PostMapping("/roles/page")
    @ApiOperation("分页查询角色")
    public Result<Page<AuthorityRolePO>> pageRoles (@RequestBody PageParam page) {
        Page<AuthorityRolePO> pageResult = authorityRoleService.pageRoles(page);
        return Result.ofSuccess(pageResult);
    }
}
