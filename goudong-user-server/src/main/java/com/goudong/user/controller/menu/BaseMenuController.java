package com.goudong.user.controller.menu;

import com.goudong.core.lang.Result;
import com.goudong.user.dto.AddMenuReq;
import com.goudong.user.dto.BaseMenuDTO;
import com.goudong.user.dto.BaseMenuPageReq;
import com.goudong.user.dto.InitMenuReq;
import com.goudong.user.service.BaseMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 类描述：
 * 菜单
 * @author cfl
 * @version 1.0
 * @date 2022/9/13 20:53
 */
@Api(tags = "菜单")
@Slf4j
@RestController
@RequestMapping("/base-menu")
@RequiredArgsConstructor
public class BaseMenuController {
    //~fields
    //==================================================================================================================
    private final BaseMenuService baseMenuService;
    //~methods
    //==================================================================================================================
    @PostMapping("/init")
    @ApiOperation(value = "初始化")
    public Result<List<BaseMenuDTO>> init(@RequestBody List<InitMenuReq> req) {
        return Result.ofSuccess(baseMenuService.init(req));
    }

    @GetMapping("/tree")
    @ApiOperation(value = "查询所有菜单")
    public Result<List<BaseMenuDTO>> listByTree(@Validated BaseMenuPageReq req) {
        return Result.ofSuccess(baseMenuService.listByTree(req));
    }

    @PostMapping
    @ApiOperation(value = "添加单个菜单")
    public Result<BaseMenuDTO> addMenu(@RequestBody AddMenuReq req) {
        return Result.ofSuccess(baseMenuService.addMenu(req));
    }
}
