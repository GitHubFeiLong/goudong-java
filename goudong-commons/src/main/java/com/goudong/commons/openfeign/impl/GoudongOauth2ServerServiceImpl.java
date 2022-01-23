package com.goudong.commons.openfeign.impl;

import com.goudong.commons.dto.oauth2.BaseUserDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelist2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseWhitelistDTO;
import com.goudong.commons.frame.core.Result;
import com.goudong.commons.openfeign.GoudongOauth2ServerService;

import java.util.List;

/**
 * 类描述：
 * 认证服务的实现
 * @author msi
 * @date 2022/1/20 22:29
 * @version 1.0
 */
public class GoudongOauth2ServerServiceImpl implements GoudongOauth2ServerService {


    /**
     * 新增白名单
     *
     * @param createDTOS
     * @return
     */
    @Override
    public Result<List<BaseWhitelistDTO>> addWhitelist(List<BaseWhitelist2CreateDTO> createDTOS) {
        return null;
    }

    /**
     * 查询所有白名单
     *
     * @return
     */
    @Override
    public Result<List<BaseWhitelistDTO>> listWhitelist() {
        return null;
    }

    /**
     * 初始化白名单到Redis中去
     *
     * @return
     */
    @Override
    public Result<List<BaseWhitelistDTO>> initWhitelist2Redis() {
        return null;
    }

    /**
     * 获取当前用户
     *
     * @return
     */
    @Override
    public Result<BaseUserDTO> currentUser() {
        return null;
    }

    /**
     * 鉴权
     *
     * @param uri
     * @param method
     * @return
     */
    @Override
    public Result<BaseUserDTO> authorize(String uri, String method) {
        return null;
    }
}
