package com.goudong.user.service;

import com.goudong.commons.dto.user.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.user.BaseWhitelistDTO;

import java.util.List;

/**
 * 接口描述：
 * 白名单业务层接口
 * @author msi
 * @version 1.0
 * @date 2022/1/9 3:14
 */
public interface BaseWhitelistService {

    /**
     * 添加白名单
     * @param createDTOS 新增白名单接口
     */
    List<BaseWhitelistDTO> addWhitelist(List<BaseWhitelist2CreateDTO> createDTOS);
}
