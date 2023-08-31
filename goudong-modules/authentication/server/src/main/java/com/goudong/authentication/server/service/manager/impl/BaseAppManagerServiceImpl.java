package com.goudong.authentication.server.service.manager.impl;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.service.BaseAppService;
import com.goudong.authentication.server.service.manager.BaseAppManagerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 类描述：
 *
 * @Author Administrator
 * @Version 1.0
 */
@Service
public class BaseAppManagerServiceImpl implements BaseAppManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseAppService baseAppService;

    //~methods
    //==================================================================================================================
    /**
     * 根据{@code appId}查询应用
     *
     * @param appId 应用id
     * @return 应用对象
     */
    @Override
    public BaseApp findById(Long appId) {
        return baseAppService.findById(appId);
    }
}
