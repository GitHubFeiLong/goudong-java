package com.goudong.wx.central.control.service.impl;

import com.goudong.wx.central.control.properties.WxAppProperties;
import com.goudong.wx.central.control.service.AccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 类描述：
 * 获取Access Token的服务层接口实现
 * @author cfl
 * @version 1.0
 * @date 2023/3/14 20:13
 */
@Slf4j
@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    //~fields
    //==================================================================================================================
    @Resource
    private WxAppProperties wxAppProperties;

    //~methods
    //==================================================================================================================
    /**
     * 根据appId获取Access Token
     *
     * @param appId
     * @return
     */
    @Override
    public Object getAccessToken(String appId) {
        log.info("{}", wxAppProperties);
        return null;
    }
}
