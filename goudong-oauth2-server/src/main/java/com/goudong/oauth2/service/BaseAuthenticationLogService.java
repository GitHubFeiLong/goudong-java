package com.goudong.oauth2.service;

import com.goudong.oauth2.dto.BaseAuthenticationLogDTO;
import com.goudong.oauth2.po.BaseAuthenticationLogPO;

/**
 * 接口描述：
 * 用户认证日志服务层接口
 * @author msi
 * @version 1.0
 * @date 2022/2/4 12:26
 */
public interface BaseAuthenticationLogService {
    /**
     * 创建一条认证日志
     * @param authenticationLogDTO
     * @return
     */
    BaseAuthenticationLogPO create(BaseAuthenticationLogDTO authenticationLogDTO);

}
