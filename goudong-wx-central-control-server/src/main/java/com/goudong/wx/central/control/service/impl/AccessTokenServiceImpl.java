package com.goudong.wx.central.control.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.BasicException;
import com.goudong.wx.central.control.constant.LockKeyConst;
import com.goudong.wx.central.control.dto.AccessTokenDTO;
import com.goudong.wx.central.control.dto.resp.AccessTokenResp;
import com.goudong.wx.central.control.enumerate.RedisKeyProviderEnum;
import com.goudong.wx.central.control.properties.WxAppProperties;
import com.goudong.wx.central.control.service.AccessTokenService;
import com.goudong.wx.central.control.util.WxRequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
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

    @Resource
    private RedisTool redisTool;

    @Resource
    private RedissonClient redissonClient;

    //~methods
    //==================================================================================================================
    /**
     * 根据appId获取Access Token
     *
     * @param appId
     * @return
     */
    @Override
    public AccessTokenDTO getAccessToken(String appId) {
        log.info("{}", wxAppProperties);
        String appSecret = Optional.ofNullable(wxAppProperties.getAppMap().get(appId)).orElseThrow(() -> BasicException.client("appId无效"));
        AccessTokenDTO dto = null;
        // 查询redis是否存在数据
        boolean existKey = redisTool.existKey(RedisKeyProviderEnum.ACCESS_TOKEN, appId);
        if (existKey) {
            dto = (AccessTokenDTO)redisTool.get(RedisKeyProviderEnum.ACCESS_TOKEN, appId);
            // redis存在，token还处于有效期
            if (dto != null && dto.getExpiresTime() > System.currentTimeMillis()) {
                return dto;
            }
        }

        /*
            分布式锁
         */
        RLock lock = redissonClient.getLock(LockKeyConst.getKey(LockKeyConst.LOCK_GET_ACCESS_TOKEN, appId));
        try {
            lock.lock();
            // 调用微信api，获取token
            AccessTokenResp accessToken = WxRequestUtil.getAccessToken(appId, appSecret);
            dto = BeanUtil.copyProperties(accessToken, AccessTokenDTO.class);
            redisTool.set(RedisKeyProviderEnum.ACCESS_TOKEN, dto, appId);
            return dto;
        } finally {
            lock.unlock();
        }
    }
}
