package com.goudong.user.controller.whitelist;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.dto.user.BaseWhitelist2CreateDTO;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.utils.core.LogUtil;
import com.goudong.user.po.BaseWhitelistPO;
import com.goudong.user.repository.BaseWhitelistRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    private final BaseWhitelistRepository baseWhitelistRepository;

    public BaseWhitelistController(BaseWhitelistRepository baseWhitelistRepository) {
        this.baseWhitelistRepository = baseWhitelistRepository;
    }


    /**
     *
     * @param createDTOS 需要保存或更新的白名单集合
     * @return
     */
    @ApiOperation(value = "保存白名单", notes = "该接口将查询所有的白名单，并和参数进行一一比对，然后再决定进行更新或新增白名单")
    @PostMapping("/")
    @Transactional
    public Result addWhitelist(List<BaseWhitelist2CreateDTO> createDTOS) {
        /*
            参数校验
         */
        if (CollectionUtils.isEmpty(createDTOS)) {
            LogUtil.warn(log, "添加白名单接口，参数为空：{}", createDTOS);
            return Result.ofSuccess();
        }
        // 不为空，具体校验 method
        createDTOS.stream().

        List<BaseWhitelistPO> baseWhitelistPOS = baseWhitelistRepository.findAll();
        List<BaseWhitelistPO> var0 = BeanUtil.copyToList(createDTOS, BaseWhitelistPO.class, CopyOptions.create());
        // 数据库没数据直接保存
        if (CollectionUtils.isEmpty(baseWhitelistPOS)) {
            baseWhitelistRepository.saveAll(var0);
            return Result.ofSuccess(var0);
        }

        return Result.ofSuccess();
    }
}
