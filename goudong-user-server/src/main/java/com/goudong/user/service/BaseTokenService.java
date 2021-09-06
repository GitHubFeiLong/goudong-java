package com.goudong.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.goudong.commons.dto.BaseTokenDTO;
import com.goudong.commons.po.BaseTokenPO;

import java.util.List;

/**
 * 接口描述：
 * baseToken服务接口
 * @author msi
 * @version 1.0
 * @date 2021/9/5 22:01
 */
public interface BaseTokenService extends IService<BaseTokenPO> {

    /**
     * 新增多条用户token信息
     * @param baseTokenDTOS
     * @return
     */
    List<BaseTokenDTO> createTokens(List<BaseTokenDTO> baseTokenDTOS);

    /**
     * 根据token md5 查询
     * @param tokenMd5
     * @return
     */
    BaseTokenDTO getTokenByTokenMd5(String tokenMd5);
}
