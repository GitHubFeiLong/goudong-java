package com.goudong.oauth2.service;

import com.goudong.commons.dto.oauth2.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;

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

    /**
     * 查询所有白名单
     * @return
     */
    List<BaseWhitelistDTO> findAll();
}
