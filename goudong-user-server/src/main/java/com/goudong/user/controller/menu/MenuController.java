package com.goudong.user.controller.menu;

import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.framework.core.Result;
import com.goudong.user.dto.InitMenuReq;
import com.goudong.user.service.BaseMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 类描述：
 * 菜单
 * @author cfl
 * @version 1.0
 * @date 2022/9/13 20:53
 */
@Api(tags = "菜单相关接口")
@Slf4j
@RestController
@RequestMapping("/base-menu")
@RequiredArgsConstructor
public class MenuController {
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

}
