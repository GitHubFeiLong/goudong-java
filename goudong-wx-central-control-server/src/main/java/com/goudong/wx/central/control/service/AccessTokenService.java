package com.goudong.wx.central.control.service;

import com.goudong.wx.central.control.dto.AccessTokenDTO;

/**
 * 接口描述：
 * 获取Access Token的服务层接口
 * @author Administrator
 * @version 1.0
 * @date 2023/3/14 20:12
 */
public interface AccessTokenService {
    //~methods
    //==================================================================================================================

    /**
     * 根据appId获取Access Token
     * @param appId
     * @return
     */
    AccessTokenDTO getAccessToken(String appId);
}
