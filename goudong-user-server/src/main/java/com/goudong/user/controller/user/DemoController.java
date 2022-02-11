package com.goudong.user.controller.user;

import com.goudong.commons.annotation.core.Whitelist;
import com.goudong.commons.annotation.validator.EnumValidator;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.utils.core.AssertUtil;
import com.goudong.user.enumerate.DemoEnum;
import com.goudong.user.enumerate.RedisKeyProviderEnum;
import com.goudong.user.po.BaseUserPO;
import com.goudong.user.service.BaseUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/2/11 16:05
 */
@Api(tags = "测试")
@Slf4j
@Validated
@RestController
@RequestMapping("/demo")
public class DemoController {
    @PostMapping("/demo")
    @ApiOperation(value = "检查手机号", notes = "检查手机号是否可以使用，true可以使用")
    @ApiImplicitParam(name = "phone", value = "手机号")
    @Whitelist("根据手机号获取账号")
    public Result<Boolean> getUserByPhone(@Validated()@RequestBody A a ) {

        return Result.ofSuccess(true);
    }

    @Data
    static class A {
        @EnumValidator(enumClass = DemoEnum.class, groups = BaseUserService.class)
        private String name;
    }
}
