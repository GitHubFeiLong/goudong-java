package com.goudong.oauth2.controller;

import com.goudong.commons.dto.oauth2.BaseApiResource2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseApiResourceDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.framework.core.Result;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.oauth2.service.BaseApiResourceService;
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
 * 保存系统中所有api接口资源 控制层
 * @author cfl 
 * @date 2022/8/2 22:54 
 * @version 1.0
 */
@Api(tags = "api接口资源")
@Slf4j
@Validated
@RestController
@RequestMapping("/api-resource")
public class BaseApiResourceController {

    /**
     * 白名单服务层接口
     */
    private final BaseApiResourceService baseApiResourceService;

    public BaseApiResourceController(BaseApiResourceService baseApiResourceService) {
        this.baseApiResourceService = baseApiResourceService;
    }

    /**
     * 保存api接口资源
     * @param createDTOS 需要保存或更新的接口集合
     * @return
     */
    @ApiOperation(value = "保存api接口资源")
    @PostMapping("/api-resource")
    @Transactional
    public Result<List<BaseApiResourceDTO>> addApiResources(@RequestBody List<BaseApiResource2CreateDTO> createDTOS) {
        /*
            参数校验
         */
        if (CollectionUtils.isEmpty(createDTOS)) {
            LogUtil.warn(log, "添加api资源，参数为空：{}", createDTOS);
            return Result.ofSuccess(new ArrayList<>());
        }

        // 校验application_name
        int size = createDTOS.stream().map(m -> m.getApplicationName()).collect(Collectors.toSet()).size();
        if (size > 1) {
            throw ClientException.client(ClientExceptionEnum.BAD_REQUEST, "参数applicationName错误");
        }

        // method修改成大写
        createDTOS.stream().forEach(p->{
            p.setMethod(p.getMethod().toUpperCase());
        });

        List<BaseApiResourceDTO> baseWhitelistDTOS = baseApiResourceService.addApiResources(createDTOS);

        return Result.ofSuccess(baseWhitelistDTOS);
    }
}
