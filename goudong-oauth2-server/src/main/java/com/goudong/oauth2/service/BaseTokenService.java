package com.goudong.oauth2.service;


import com.goudong.oauth2.dto.BaseTokenDTO;

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

}
