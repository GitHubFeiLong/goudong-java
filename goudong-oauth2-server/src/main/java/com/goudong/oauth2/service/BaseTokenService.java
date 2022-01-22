package com.goudong.oauth2.service;


import com.goudong.oauth2.dto.BaseTokenDTO;
import com.goudong.oauth2.po.BaseTokenPO;
import org.springframework.data.domain.Example;

/**
 * 接口描述：
 * 令牌服务接口
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:28
 */
public interface BaseTokenService {
    //~methods
    //==================================================================================================================

    /**
     * 保存令牌到数据库
     * @param dto
     * @return
     */
    BaseTokenDTO save(BaseTokenDTO dto);

    /**
     * 根据访问令牌,和客户端类型获取令牌信息
     * @param accessToken 访问令牌
     * @param clientType 客户端类型
     * @return
     */
    BaseTokenDTO findByAccessTokenAndClientType(String accessToken, String clientType);

    /**
     * 根据构建的example查询令牌
     * @param example 构造查询对象
     * @return
     */
    BaseTokenDTO findByExample(Example<BaseTokenPO> example);
}
