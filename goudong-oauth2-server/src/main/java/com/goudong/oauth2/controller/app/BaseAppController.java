package com.goudong.oauth2.controller.app;

import com.goudong.core.lang.Result;
import com.goudong.oauth2.dto.BaseAppApplyReq;
import com.goudong.oauth2.dto.BaseAppAuditReq;
import com.goudong.oauth2.dto.BaseAppDTO;
import com.goudong.oauth2.service.BaseAppService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 类描述：
 * 应用
 * @author cfl
 * @date 2023/6/10 22:49
 * @version 1.0
 */
@Api(tags = "APP")
@Slf4j
@Validated
@RestController
@RequestMapping("/base-app")
@RequiredArgsConstructor
public class BaseAppController {
    //~fields
    //==================================================================================================================
    private final BaseAppService baseAppService;

    @PostMapping
    @ApiOperation("申请应用")
    public Result<BaseAppDTO> apply(@RequestBody @Validated BaseAppApplyReq req) {
        return Result.ofSuccess(baseAppService.apply(req));
    }

    @DeleteMapping("/{id}")
    @ApiOperation("删除应用")
    public Result<Boolean> deleteById(@PathVariable("id") Long id) {
        return Result.ofSuccess(baseAppService.deleteById(id));
    }

    @PutMapping("/audit")
    @ApiOperation("审核应用")
    public Result<BaseAppDTO> audit(@RequestBody @Validated BaseAppAuditReq req) {
        return Result.ofSuccess(baseAppService.audit(req));
    }
}
