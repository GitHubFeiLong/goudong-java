package com.goudong.wx.central.control.service.impl;

import com.goudong.boot.web.core.BasicException;
import com.goudong.wx.central.control.properties.WxAppProperties;
import com.goudong.wx.central.control.service.AccessTokenService;
import com.goudong.wx.central.control.util.WxRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Optional;

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
        String appSecret = Optional.ofNullable(wxAppProperties.getAppMap().get(appId)).orElseThrow(() -> BasicException.client("appId无效"));
        String accessToken = WxRequestUtil.getAccessToken(appId, appSecret);
        return null;
    }
}
