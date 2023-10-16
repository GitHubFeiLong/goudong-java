package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.service.dto.PermissionDTO;
import com.goudong.authentication.server.service.manager.PermissionManagerService;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述：
 * 权限资源控制器
 * @author chenf
 * @version 1.0
 */
@RestController
@RequestMapping("/permission")
@Api(tags = "权限")
@Slf4j
public class PermissionResource {
    //~fields
    //==================================================================================================================
    @Resource
    private PermissionManagerService permissionManagerService;

    //~methods
    //==================================================================================================================
    @PostMapping("/listPermission")
    @ApiOperation(value = "获取权限", notes = "查询应用的权限列表（资源对应的能访问的角色）")
    public Result<List<PermissionDTO>> listPermission() {
        return Result.ofSuccess(permissionManagerService.listPermission());
    }
}
