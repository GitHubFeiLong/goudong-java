package com.zhy.authentication.server.rest;

import com.goudong.core.lang.Result;
import com.zhy.authentication.server.rest.req.search.BaseAppDropDown;
import com.zhy.authentication.server.service.BaseAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

import static com.zhy.authentication.server.constant.SwaggerConst.DROP_DOWN_GROUP_NAME;

/**
 * 类描述：
 * 下拉菜单
 * @ClassName DropDownResource
 * @Author Administrator
 * @Date 2023/8/3 21:25
 * @Version 1.0
 */
@RestController
@RequestMapping("/drop-down")
@Api(tags = "下拉管理")
public class DropDownResource {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppService baseAppService;

    //~methods
    //==================================================================================================================
    /**
     * 应用下拉
     * @param req
     * @return
     */
    @GetMapping("/base-app")
    @ApiOperation(value = "应用下拉(条件限制)", notes = "需要登录，显示当前用户能查询到的应用，超级管理员查询所有，管理员只能查询本应用", tags = DROP_DOWN_GROUP_NAME)
    public Result<List<BaseAppDropDown>> dropDown(@Validated BaseAppDropDown req) {
        return Result.ofSuccess(baseAppService.dropDown(req));
    }

    /**
     * 应用下拉
     * @param req
     * @return
     */
    @GetMapping("/base-app/all-drop-down")
    @ApiOperation(value = "应用下拉(无条件限制)", notes = "不需要登录，显示所有应用，根据创建时间排序", tags = DROP_DOWN_GROUP_NAME)
    public Result<List<BaseAppDropDown>> allDropDown(@Validated BaseAppDropDown req) {
        return Result.ofSuccess(baseAppService.allDropDown(req));
    }
}
