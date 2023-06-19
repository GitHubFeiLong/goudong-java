package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.goudong.boot.redis.core.RedisKeyProvider;
import com.goudong.boot.redis.core.RedisTool;
import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.oauth2.core.TokenExpires;
import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.dto.authentication.BaseUserDTO;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.exception.RefreshTokenExpiredException;
import com.goudong.oauth2.exception.RefreshTokenInvalidException;
import com.goudong.oauth2.po.BaseTokenPO;
import com.goudong.oauth2.po.BaseUserPO;
import com.goudong.oauth2.properties.TokenExpiresProperties;
import com.goudong.oauth2.repository.BaseTokenRepository;
import com.goudong.oauth2.service.BaseTokenService;
import com.goudong.oauth2.service.BaseUserService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 接口描述：
 * 令牌服务实现,用户登录，退出，刷新令牌
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:28
 */
@Service
public class BaseTokenServiceImpl implements BaseTokenService {

    //~fields
    //==================================================================================================================
    /**
     * 令牌持久层
     */
    private final BaseTokenRepository baseTokenRepository;

    /**
     * 自定义配置令牌失效
     */
    private final TokenExpiresProperties tokenExpiresProperties;

    /**
     * 请求对象
     */
    private final HttpServletRequest httpServletRequest;

    /**
     * 用户服务
     */
    private final BaseUserService baseUserService;

    /**
     * redis tool
     */
    private final RedisTool redisTool;

    //~methods
    //==================================================================================================================
    public BaseTokenServiceImpl(BaseTokenRepository baseTokenRepository,
                                TokenExpiresProperties tokenExpiresProperties,
                                HttpServletRequest httpServletRequest,
                                @Lazy BaseUserService baseUserService,
                                RedisTool redisTool) {
        this.baseTokenRepository = baseTokenRepository;
        this.tokenExpiresProperties = tokenExpiresProperties;
        this.httpServletRequest = httpServletRequest;
        this.baseUserService = baseUserService;
        this.redisTool = redisTool;
    }

    /**
     * 用户登录成功或使用刷新Token，获取新的令牌。并将其令牌保存到Redis和Mysql中
     * 根据设置是否允许重复登录配置，判断删除已有的令牌
     * @param appId 应用id
     * @param userId 用户id
     * @return
     */
    @Override
    @Transactional
    public BaseTokenDTO loginHandler(Long appId, Long userId) {
        ClientSideEnum clientSideEnum = ClientSideEnum.getClientSide(httpServletRequest);

        BaseTokenPO baseTokenPO = new BaseTokenPO();
        baseTokenPO.setAccessToken(IdUtil.simpleUUID());
        baseTokenPO.setRefreshToken(IdUtil.simpleUUID());
        baseTokenPO.setUserId(userId);
        baseTokenPO.setAppId(appId);
        baseTokenPO.setClientType(clientSideEnum.getLowerName());
        baseTokenPO.setDeleted(false);
        baseTokenPO.setCreateUserId(userId);
        baseTokenPO.setUpdateUserId(userId);
        baseTokenPO.setCreateTime(new Date());
        baseTokenPO.setUpdateTime(new Date());
        // 默认是 browser
        TokenExpires tokenExpires = TokenExpires.getTokenExpires(clientSideEnum, tokenExpiresProperties);

        // 处理过期时间和类型
        disposeToken(tokenExpires, baseTokenPO);

        // 根据用户id,和客户端类型 查询数据
        if (tokenExpiresProperties.isDisableRepeatLogin()) {
            List<BaseTokenPO> baseTokenPOS = baseTokenRepository.findAllByUserIdAndClientType(userId, clientSideEnum.getLowerName());
            if (CollectionUtil.isNotEmpty(baseTokenPOS)) {
                // 删除MySQL中的数据
                baseTokenRepository.deleteAll(baseTokenPOS);

                // 删除redis中无用数据
                List<RedisKeyProvider> deleteKes = new ArrayList<>(baseTokenPOS.size());
                List<List<Object>> params = new ArrayList<>();
                baseTokenPOS.stream().forEach(tokenPO->{
                    deleteKes.add(RedisKeyProviderEnum.AUTHENTICATION);
                    params.add(Lists.newArrayList(tokenPO.getClientType(), tokenPO.getAccessToken()));
                });
                redisTool.deleteKeys(deleteKes, params);
            }
        }

        // Hibernate 在实际执行SQL语句时并没有按照代码的顺序执行，而是按照 INSERT, UPDATE, DELETE的顺序执行的
        // 先执行flush()
        baseTokenRepository.flush();
        // 新建
        this.baseTokenRepository.save(baseTokenPO);

        BaseTokenDTO baseTokenDTO = BeanUtil.copyProperties(baseTokenPO, BaseTokenDTO.class);

        // 设置sessionId
        baseTokenDTO.setSessionId(baseTokenDTO.getAccessToken());
        return baseTokenDTO;
    }


    /**
     * 处理生成token
     * @param tokenExpires 令牌失效配置对象
     * @param baseTokenPO 令牌对象
     */
    private void disposeToken(TokenExpires tokenExpires, BaseTokenPO baseTokenPO) {
        LocalDateTime now = LocalDateTime.now();
        // 设置过期时长
        baseTokenPO.setAccessExpires(
                Date.from(
                        now.plusSeconds(tokenExpires.getAccessTimeUnit().toSeconds(tokenExpires.getAccess()))
                                .atZone( ZoneId.systemDefault()).toInstant()
                )
        );
        baseTokenPO.setRefreshExpires(
                Date.from(
                        now.plusSeconds(tokenExpires.getRefreshTimeUnit().toSeconds(tokenExpires.getRefresh()))
                                .atZone( ZoneId.systemDefault()).toInstant()
                )
        );
    }

    /**
     * 根据访问令牌,和客户端类型获取令牌信息
     *
     * @param accessToken 访问令牌
     * @param clientType  客户端类型
     * @return
     */
    @Override
    public BaseTokenDTO findByAccessTokenAndClientType(String accessToken, String clientType) {
        return BeanUtil.copyProperties(baseTokenRepository.findByAccessTokenAndClientType(accessToken, clientType), BaseTokenDTO.class);
    }

    /**
     * 用户退出登录时，删除Redis中令牌和Mysql中的令牌
     *
     * @param accessToken 访问令牌
     * @param clientType  客户端类型
     * @return
     */
    @Override
    public BaseTokenDTO logout(String accessToken, String clientType) {
        BaseTokenPO baseTokenPO = baseTokenRepository.findByAccessTokenAndClientType(accessToken, clientType);
        if (baseTokenPO != null) {
            baseTokenRepository.delete(baseTokenPO);
        }

        return BeanUtil.copyProperties(baseTokenPO, BaseTokenDTO.class);
    }

    /**
     * 根据刷新令牌，生成新的令牌,之前的令牌设置为无效
     *
     * @param refreshToken 刷新令牌
     * @return
     */
    @Override
    @Transactional
    public BaseTokenDTO refreshToken(String refreshToken) {
        /*
            获取令牌详细信息
         */
        BaseTokenPO byRefreshToken = baseTokenRepository.findByRefreshToken(refreshToken).orElseThrow(() -> new RefreshTokenInvalidException("refreshToken令牌无效"));

        // 判断刷新Token是否过期
        Date refreshExpires = byRefreshToken.getRefreshExpires();
        AssertUtil.isTrue(refreshExpires.after(new Date()), () -> new RefreshTokenExpiredException(String.format("令牌已经过期refreshExpires=%s", refreshExpires)));

        // 未过期，就需要生成新的token，并将原有的token删除（使之无效）
        Long userId = byRefreshToken.getUserId();
        BaseTokenDTO baseTokenDTO = this.loginHandler(byRefreshToken.getAppId(), userId);

        // 保存accessToken和用户信息到redis中
        BaseUserDTO baseUser = baseUserService.findById(userId);
        baseUserService.saveAccessToken2Redis(BeanUtil.copyProperties(baseUser, BaseUserPO.class, "password")
                , baseTokenDTO.getAccessToken());

            /*
                将原refreshToken对应的accessToken删除（MySQL、Redis）,跟重复登录没任何关系
             */
        // 删除Mysql中的数据
        baseTokenRepository.delete(byRefreshToken);
        // 删除Redis中的数据
        redisTool.deleteKey(RedisKeyProviderEnum.AUTHENTICATION, byRefreshToken.getAppId(), byRefreshToken.getClientType(), byRefreshToken.getAccessToken());
        return baseTokenDTO;
    }


}
