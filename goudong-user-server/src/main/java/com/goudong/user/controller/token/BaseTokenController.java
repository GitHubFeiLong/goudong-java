package com.goudong.user.controller.token;

import com.goudong.commons.dto.BaseTokenDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.vo.BaseToken2CreateVO;
import com.goudong.user.service.BaseTokenService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 类描述：
 * 系统颁发的token处理
 * @Author msi
 * @Date 2021-05-02 13:33
 * @Version 1.0
 */
@Api(tags = "token")
@Slf4j
@Validated
@RestController
@RequestMapping("/base-token")
public class BaseTokenController {

    @Resource
    private BaseTokenService baseTokenService;

    @PostMapping("/tokens")
    @ApiOperation(value = "新增多条token")
    public Result<List<BaseTokenDTO>> createTokens (@RequestBody List<BaseToken2CreateVO> token2CreateVOS ) {
        List<BaseTokenDTO> baseTokenDTOS = BeanUtil.copyList(token2CreateVOS, BaseTokenDTO.class);
        return Result.ofSuccess(baseTokenService.createTokens(baseTokenDTOS));
    }

}
