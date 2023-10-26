package com.goudong.authentication.server.rest;

import com.goudong.authentication.server.rest.req.BaseMenuImportReq;
import com.goudong.authentication.server.rest.req.BaseRoleImportReq;
import com.goudong.authentication.server.rest.req.BaseUserExportReq;
import com.goudong.authentication.server.rest.req.BaseUserImportReq;
import com.goudong.authentication.server.rest.resp.BaseImportResp;
import com.goudong.authentication.server.rest.resp.BaseUserImportResp;
import com.goudong.authentication.server.service.manager.ImportExportManagerService;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 导入导出
 * @author chenf
 * @version 1.0
 */

@Api(tags = "导入导出接口")
@Controller
@RequestMapping("/import-export")
public class ImportExportResource {
    //~fields
    //==================================================================================================================
    @Resource
    private ImportExportManagerService importExportManagerService;

    //~methods
    //==================================================================================================================

    @GetMapping("/export-user-template")
    @ApiOperation("用户模板导出")
    public void exportUserTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-user.xlsx");
    }

    @PostMapping("/import-user")
    @ApiOperation("用户导入")
    @ResponseBody
    public Result<Boolean> importUser(@Validated BaseUserImportReq req) {
        return Result.ofSuccess(importExportManagerService.importUser(req));
    }

    @PostMapping("/export-user")
    @ApiOperation("用户导出")
    public void exportUser(HttpServletResponse response, @RequestBody BaseUserExportReq req) {
        importExportManagerService.exportUser(response, req);
    }

    @GetMapping("/export-role-template")
    @ApiOperation("角色模板导出")
    public void exportRoleTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-role.xlsx");
    }

    @PostMapping("/import-role")
    @ApiOperation("角色导入")
    @ResponseBody
    public Result<Boolean> importRole(@Validated BaseRoleImportReq req) {
        return Result.ofSuccess(importExportManagerService.importRole(req));
    }

    @GetMapping("/export-menu-template")
    @ApiOperation("菜单模板导出")
    public void exportMenuTemplate(HttpServletResponse response) throws IOException {
        importExportManagerService.exportTemplateHandler(response, "template-menu.xlsx");
    }

    @PostMapping("/import-menu")
    @ApiOperation("菜单导入")
    @ResponseBody
    public Result<Boolean> importMenu(@Validated BaseMenuImportReq req) {
        return Result.ofSuccess(importExportManagerService.importMenu(req));
    }
}
