package com.goudong.user.controller.oauth;

import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.pojo.Result;
import com.goudong.commons.service.CommonsIgnoreResourceService;
import com.goudong.commons.utils.BeanUtil;
import com.goudong.commons.vo.BaseIgnoreResourceVO;
import com.goudong.user.service.IgnoreResourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 类描述：
 * 白名单接口，authority_ignore_resource 表的接口
 * @Author msi
 * @Date 2021-08-14 11:40
 * @Version 1.0
 */
@Api(tags = "白名单")
@Slf4j
@Validated
@RestController
@RequestMapping("/ignore-resource")
public class IgnoreResourceController {

    @Resource
    private IgnoreResourceService ignoreResourceService;

    @Resource
    private CommonsIgnoreResourceService commonsIgnoreResourceService;


    @PostMapping("/ignore-resources")
    @ApiOperation(value = "添加白名单数据", notes = "该接口，系统内部也会调用")
    public Result<List<BaseIgnoreResourceDTO>> addList (@RequestBody @Valid List<BaseIgnoreResourceVO> insertVOList) {
        List<BaseIgnoreResourceDTO> insertDTOList = BeanUtil.copyList(insertVOList, BaseIgnoreResourceDTO.class);
        List<BaseIgnoreResourceDTO> result = commonsIgnoreResourceService.addList(insertDTOList);
        return Result.ofSuccess(result);
    }

}
