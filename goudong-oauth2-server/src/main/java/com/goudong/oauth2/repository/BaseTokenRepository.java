package com.goudong.oauth2.repository;

import com.goudong.oauth2.po.BaseTokenPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:27
 */
public interface BaseTokenRepository extends JpaRepository<BaseTokenPO, Long>, JpaSpecificationExecutor<BaseTokenPO> {

    /**
     * 根据用户id和客户端类型查询
     * @param userId 用户表主键
     * @param clientType 客户端类型
     * @return
     */
    List<BaseTokenPO> findAllByUserIdAndClientType(Long userId, String clientType);

    /**
     * 根据accessToken查询令牌信息
     * @param accessToken 令牌信息
     * @param clientType 客户端类型
     * @return
     */
    BaseTokenPO findByAccessTokenAndClientType(String accessToken, String clientType);

    /**
     * 根据刷新令牌查询令牌信息
     * @param refreshToken 刷新令牌
     * @return
     */
    BaseTokenPO findByRefreshToken(String refreshToken);
}
