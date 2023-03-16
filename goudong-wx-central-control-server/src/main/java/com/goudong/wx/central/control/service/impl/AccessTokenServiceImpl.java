package com.goudong.wx.central.control.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.boot.web.core.BasicException;
import com.goudong.wx.central.control.constant.LockKeyConst;
import com.goudong.wx.central.control.dto.AccessTokenDTO;
import com.goudong.wx.central.control.dto.req.GetStableAccessTokenReq;
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
        String appSecret = getAppSecretByAppId(appId);
        AccessTokenDTO dto = null;
        // 查询redis是否存在数据
        boolean existKey = redisTool.existKey(RedisKeyProviderEnum.ACCESS_TOKEN, appId);
        if (existKey) {
            dto = (AccessTokenDTO)redisTool.get(RedisKeyProviderEnum.ACCESS_TOKEN, appId);
            // redis存在，token还处于有效期
            if (dto != null && dto.getExpiresTime() != null && dto.getExpiresTime() > System.currentTimeMillis()) {
                return dto;
            }
        }

        /*
            分布式锁
         */
        RLock lock = redissonClient.getLock(LockKeyConst.getKey(LockKeyConst.LOCK_GET_ACCESS_TOKEN, appId));
        try {
            lock.lock();
            existKey = redisTool.existKey(RedisKeyProviderEnum.ACCESS_TOKEN, appId);
            if (existKey) {
                dto = (AccessTokenDTO)redisTool.get(RedisKeyProviderEnum.ACCESS_TOKEN, appId);
                // redis存在，token还处于有效期
                if (dto != null && dto.getExpiresTime() != null && dto.getExpiresTime() > System.currentTimeMillis()) {
                    return dto;
                }
            }
            // 调用微信api，获取token
            AccessTokenResp resp = WxRequestUtil.getAccessToken(appId, appSecret);

            // 判断当前获取的token有效期是否有4000s剩余时间（时间太短了就重新获取新的token）
            if (resp.getExpiresIn() < 4000) {
                // 强制刷新
                resp = WxRequestUtil.getStableAccessToken(new GetStableAccessTokenReq(appId, appSecret, true));
            }

            dto = BeanUtil.copyProperties(resp, AccessTokenDTO.class);
            redisTool.set(RedisKeyProviderEnum.ACCESS_TOKEN, dto, appId);
            return dto;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 根据appId 刷新 Access Token
     *
     * @param appId
     * @return
     */
    @Override
    public AccessTokenDTO refreshAccessToken(String appId) {
        String appSecret = getAppSecretByAppId(appId);
        AccessTokenDTO dto = null;
        /*
            分布式锁
         */
        RLock lock = redissonClient.getLock(LockKeyConst.getKey(LockKeyConst.LOCK_GET_ACCESS_TOKEN, appId));
        try {
            lock.lock();
            // 调用微信api，刷新并获取新的token
            GetStableAccessTokenReq req = new GetStableAccessTokenReq(appId, appSecret, true);
            AccessTokenResp accessToken = WxRequestUtil.getStableAccessToken(req);
            dto = BeanUtil.copyProperties(accessToken, AccessTokenDTO.class);
            redisTool.set(RedisKeyProviderEnum.ACCESS_TOKEN, dto, appId);
            return dto;
        } finally {
            lock.unlock();
        }
    }

    /**
     * 根据appId获取appSecret
     * @param appId
     * @return
     */
    private String getAppSecretByAppId(String appId) {
        return Optional.ofNullable(wxAppProperties.getAppMap().get(appId)).orElseThrow(() -> BasicException.client("appId无效"));
    }
}
