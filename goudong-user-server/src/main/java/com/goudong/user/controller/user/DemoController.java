package com.goudong.user.controller.user;

import com.goudong.commons.dto.user.BaseUserDTO;
import com.goudong.core.lang.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @PostMapping("/secrypt")
    @ApiOperation(value = "测试加密", notes = "检查手机号是否可以使用，true可以使用")
    public Result<BaseUserDTO> secrypt(@RequestBody BaseUserDTO userDTO) {
        return Result.ofSuccess(userDTO);
    }
}
