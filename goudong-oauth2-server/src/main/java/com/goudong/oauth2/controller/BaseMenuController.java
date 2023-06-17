package com.goudong.oauth2.controller;

import com.goudong.boot.web.core.ClientException;
import com.goudong.boot.web.enumerate.ClientExceptionEnum;
import com.goudong.commons.constant.core.HttpMethodConst;
import com.goudong.commons.dto.oauth2.HideMenu2CreateDTO;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.core.lang.Result;
import com.goudong.core.util.CollectionUtil;
import com.goudong.oauth2.dto.authentication.BaseMenuDTO;
import com.goudong.oauth2.service.BaseMenuService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 菜单控制层
 * @author cfl
 * @version 1.0
 * @date 2022/9/19 22:14
 */
@Api(tags = "菜单")
@Slf4j
@Validated
@RestController
@RequestMapping("/base-menu")
@RequiredArgsConstructor
public class BaseMenuController {
    //~fields
    //==================================================================================================================
    private final BaseMenuService baseMenuService;

    //~methods
    //==================================================================================================================
    /**
     * 保存白名单
     * @param createDTOS 需要保存或更新的白名单集合
     * @return
     */
    @ApiOperation(value = "保存隐藏菜单", notes = "该接口将查询所有的隐藏菜单，并和参数进行一一比对，然后再决定进行更新或新增隐藏菜单")
    @PostMapping("/hide-menu")
    public Result<List<BaseMenuDTO>> addHideMenu(@RequestBody List<HideMenu2CreateDTO> createDTOS) {
        /*
            参数校验
         */
        if (CollectionUtil.isEmpty(createDTOS)) {
            LogUtil.warn(log, "保存隐藏菜单，参数为空：{}", createDTOS);
            return Result.ofSuccess(new ArrayList<>());
        }
        // 不为空，具体校验 method
        createDTOS.stream().forEach(p->{
            if (!HttpMethodConst.ALL_HTTP_METHOD.contains(p.getMethod().toUpperCase())) {
                String serverMessage = String.format("保存隐藏菜单错误，method不正确:%s", p.getMethod());
                throw ClientException.client(ClientExceptionEnum.BAD_REQUEST, "保存隐藏菜单失败",serverMessage);
            }
        });

        List<BaseMenuDTO> dtos = baseMenuService.addHideMenu(createDTOS);

        return Result.ofSuccess(dtos);

    }
}
