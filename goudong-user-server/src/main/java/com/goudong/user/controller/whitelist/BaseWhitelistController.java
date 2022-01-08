package com.goudong.user.controller.whitelist;

import com.goudong.commons.constant.core.HttpMethodConst;
import com.goudong.commons.dto.user.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.user.BaseWhitelistDTO;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.user.WhitelistException;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.user.service.BaseWhitelistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 白名单控制层
 * @author msi
 * @version 1.0
 * @date 2022/1/8 20:50
 */
@Api(tags = "白名单")
@Slf4j
@Validated
@RestController
@RequestMapping("/whitelist")
public class BaseWhitelistController {

    private final BaseWhitelistService baseWhitelistService;

    public BaseWhitelistController(BaseWhitelistService baseWhitelistService) {
        this.baseWhitelistService = baseWhitelistService;
    }


    /**
     *
     * @param createDTOS 需要保存或更新的白名单集合
     * @return
     */
    @ApiOperation(value = "保存白名单", notes = "该接口将查询所有的白名单，并和参数进行一一比对，然后再决定进行更新或新增白名单")
    @PostMapping("/whitelist")
    @Transactional
    public Result<List<BaseWhitelistDTO>> addWhitelist(@RequestBody List<BaseWhitelist2CreateDTO> createDTOS) {
        /*
            参数校验
         */
        if (CollectionUtils.isEmpty(createDTOS)) {
            LogUtil.warn(log, "添加白名单接口，参数为空：{}", createDTOS);
            return Result.ofSuccess(new ArrayList<>());
        }
        // 不为空，具体校验 method
        createDTOS.stream().forEach(p->{
            List<String> upper = p.getMethods().stream().map(m -> m.toUpperCase()).collect(Collectors.toList());
            p.setMethods(upper);
            if (!HttpMethodConst.ALL_HTTP_METHOD.containsAll(upper)) {
                String serverMessage = String.format("保存白名单错误，methods成员不正确:%s", p.getMethods());
                throw new WhitelistException(ClientExceptionEnum.BAD_REQUEST, serverMessage);
            }
        });

        List<BaseWhitelistDTO> baseWhitelistDTOS = baseWhitelistService.addWhitelist(createDTOS);

        return Result.ofSuccess(baseWhitelistDTOS);

    }
}
