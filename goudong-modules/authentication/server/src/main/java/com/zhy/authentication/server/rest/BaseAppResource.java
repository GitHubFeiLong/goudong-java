package com.zhy.authentication.server.rest;

import com.goudong.core.lang.PageResult;
import com.goudong.core.lang.Result;
import com.zhy.authentication.server.constant.RoleConst;
import com.zhy.authentication.server.rest.req.BaseAppCreate;
import com.zhy.authentication.server.rest.req.BaseAppUpdate;
import com.zhy.authentication.server.rest.req.search.BaseAppDropDown;
import com.zhy.authentication.server.rest.req.search.BaseAppPage;
import com.zhy.authentication.server.rest.req.search.BaseRoleDropDown;
import com.zhy.authentication.server.service.BaseAppService;
import com.zhy.authentication.server.service.dto.BaseAppDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.zhy.authentication.server.constant.SwaggerConst.DROP_DOWN_GROUP_NAME;

/**
 * 类描述：
 * 应用管理
 * @ClassName BaseAppResource
 * @Description 新增、删除、修改、分页、下拉
 * @Author Administrator
 * @Date 2023/7/29 11:33
 * @Version 1.0
 */
@RestController
@RequestMapping("/app")
@Api(tags = "应用管理")
@Secured(value = RoleConst.ROLE_APP_SUPER_ADMIN) // 只有该角色才能处理应用
public class BaseAppResource {

    @Resource
    private BaseAppService baseAppService;

    /**
     * 新增应用
     * @param req
     * @return
     */
    @PostMapping("/base-app")
    @ApiOperation("新增应用")
    public Result<BaseAppDTO> create(@Valid @RequestBody BaseAppCreate req) {
        BaseAppDTO result = baseAppService.save(req);
        return Result.ofSuccess(result);
    }

    /**
     * 修改应用
     * @param req
     * @return
     */
    @PutMapping("/base-app")
    @ApiOperation("修改应用")
    public Result<BaseAppDTO> update(@Valid @RequestBody BaseAppUpdate req) {
        BaseAppDTO result = baseAppService.update(req);
        return Result.ofSuccess(result);
    }

    /**
     * 删除应用
     * @param id
     * @return
     */
    @DeleteMapping("/base-app/{id}")
    @ApiOperation("删除应用")
    public Result<Boolean> delete(@PathVariable Long id) {
        baseAppService.delete(id);
        return Result.ofSuccess(true);
    }

    /**
     * 应用分页
     * @param req
     * @return
     */
    @GetMapping("/base-apps")
    @ApiOperation("分页查询应用")
    public Result<PageResult<BaseAppPage>> page(@Validated BaseAppPage req) {
        return Result.ofSuccess(baseAppService.page(req));
    }
}
