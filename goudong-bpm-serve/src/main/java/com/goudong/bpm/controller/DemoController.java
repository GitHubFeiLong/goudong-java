package com.goudong.bpm.controller;

import com.goudong.commons.annotation.aop.ApiRepeat;
import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.framework.openfeign.GoudongOauth2ServerService;
import com.goudong.commons.utils.core.LogUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/7/31 23:38
 */
@Slf4j
@Api(tags = "测试")
@RestController
@RequestMapping("/demo")
@RequiredArgsConstructor
public class DemoController {
    //~fields
    //==================================================================================================================
    private final GoudongOauth2ServerService goudongOauth2ServerService;

    //~methods
    //==================================================================================================================
    @ApiOperation(value = "测试")
    //@Inner
    @Whitelist
    @GetMapping("/demo")
    @ApiRepeat(10)
    public String demo() {
        Result<BaseUserDTO> baseUserDTOResult = goudongOauth2ServerService.currentUser();
        LogUtil.info(log, "进入控制器");
        return "hello world" + SecurityContextHolder.getContext().getAuthentication();
    }
}
