package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.utils.core.IpUtil;
import com.goudong.core.util.AssertUtil;
import com.goudong.oauth2.dto.BaseAuthenticationLogDTO;
import com.goudong.oauth2.enumerate.AuthenticationLogTypeEnum;
import com.goudong.oauth2.po.BaseAuthenticationLogPO;
import com.goudong.oauth2.repository.BaseAuthenticationLogRepository;
import com.goudong.oauth2.service.BaseAuthenticationLogService;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/2/4 12:28
 */
@Service
public class BaseAuthenticationLogServiceImpl implements BaseAuthenticationLogService {
    //~fields
    //==================================================================================================================
    /**
     * 认证日志持久层
     */
    private final BaseAuthenticationLogRepository baseAuthenticationLogRepository;

    //~methods
    //==================================================================================================================


    public BaseAuthenticationLogServiceImpl(BaseAuthenticationLogRepository baseAuthenticationLogRepository) {
        this.baseAuthenticationLogRepository = baseAuthenticationLogRepository;
    }

    /**
     * 创建一条认证日志
     *
     * @param authenticationLogDTO
     * @return
     */
    @Override
    public BaseAuthenticationLogPO create(BaseAuthenticationLogDTO authenticationLogDTO) {
        // 参数校验
        AssertUtil.isEnum(authenticationLogDTO.getType(), AuthenticationLogTypeEnum.class);
        String ipv4 = IpUtil.getStringIp(((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest());
        long longIp = IpUtil.getLongIp(ipv4);
        BaseAuthenticationLogPO baseAuthenticationLogPO = BeanUtil.copyProperties(authenticationLogDTO, BaseAuthenticationLogPO.class);
        baseAuthenticationLogPO.setAppId(authenticationLogDTO.getAppId());
        baseAuthenticationLogPO.setIp(longIp);
        baseAuthenticationLogPO.setIpv4(ipv4);
        baseAuthenticationLogPO.setDeleted(authenticationLogDTO.getDeleted());
        baseAuthenticationLogPO.setCreateTime(authenticationLogDTO.getCreateTime());
        baseAuthenticationLogPO.setCreateUserId(authenticationLogDTO.getCreateUserId());
        baseAuthenticationLogPO.setUpdateTime(authenticationLogDTO.getUpdateTime());
        baseAuthenticationLogPO.setUpdateUserId(authenticationLogDTO.getUpdateUserId());
        baseAuthenticationLogRepository.save(baseAuthenticationLogPO);
        return baseAuthenticationLogPO;
    }



}
