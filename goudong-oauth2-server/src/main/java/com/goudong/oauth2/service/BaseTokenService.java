package com.goudong.oauth2.service;


import com.goudong.oauth2.dto.BaseTokenDTO;

/**
 * 接口描述：
 * 令牌服务接口，用户登录，退出，刷新令牌
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:28
 */
public interface BaseTokenService {
    //~methods
    //==================================================================================================================
    /**
     * 用户登录成功或使用刷新Token，获取新的令牌。并将其令牌保存到Redis和Mysql中
     * 根据设置是否允许重复登录配置，判断删除已有的令牌
     * @param userId 用户id
     * @return
     */
    BaseTokenDTO loginHandler(Long userId);

    /**
     * 根据访问令牌,和客户端类型获取令牌信息
     * @param accessToken 访问令牌
     * @param clientType 客户端类型
     * @return
     */
    BaseTokenDTO findByAccessTokenAndClientType(String accessToken, String clientType);

    /**
     * 用户退出登录时，删除Redis中令牌和Mysql中的令牌
     * @param accessToken 访问令牌
     * @param clientType 客户端类型
     * @return
     */
    BaseTokenDTO logout(String accessToken, String clientType);

    /**
     * 根据刷新令牌，生成新的令牌
     * @param refreshToken 刷新令牌
     * @return
     */
    BaseTokenDTO refreshToken(String refreshToken);
}
