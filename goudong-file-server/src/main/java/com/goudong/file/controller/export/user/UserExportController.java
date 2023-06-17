package com.goudong.file.controller.export.user;

import com.goudong.commons.dto.user.BaseUser2QueryPageReq;
import com.goudong.file.service.BaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 类描述：
 * 用户模块的相关导出
 * @author cfl
 * @version 1.0
 * @date 2022/11/7 15:31
 */
@Api(tags = "导出-用户模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/user/export")
@RequiredArgsConstructor
public class UserExportController {

    private final BaseUserService userService;



    @GetMapping(value = "/user")
    @ApiOperation(value = "导出用户", notes = "参数跟分页参数基本一致")
    public void userExportExcel(BaseUser2QueryPageReq req, HttpServletResponse response) throws IOException {
        userService.userExportExcel(req, response);


    }
}
