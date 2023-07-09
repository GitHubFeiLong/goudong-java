package com.goudong.file.controller.imports;

import com.goudong.file.service.UserServerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 类描述：
 * 用户模块的相关导入
 * @author cfl
 * @version 1.0
 * @date 2022/11/20 8:41
 */
@Api(tags = "导入-用户模块")
@Slf4j
@Validated
@RestController
@RequestMapping("/user-server/import")
@RequiredArgsConstructor
public class UserServerImportController {
    //~fields
    //==================================================================================================================
    private final UserServerService userServerService;



    //~methods
    //==================================================================================================================
    @PostMapping(value = "/user")
    @ApiOperation(value = "导入用户")
    public void importUser(MultipartFile file) throws IOException {

        userServerService.importUser(file);
    }
}
