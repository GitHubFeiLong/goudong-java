package com.goudong.oauth2.service;

import com.goudong.commons.dto.oauth2.BaseApiResource2CreateDTO;
import com.goudong.commons.dto.oauth2.BaseApiResourceDTO;

import java.util.List;

/**
 * 类描述：
 * 保存系统中所有api接口资源服务层接口
 * @author cfl
 * @date 2022/8/2 22:48
 * @version 1.0
 */
public interface BaseApiResourceService {

    /**
     * 保存服务api资源
     * @param createDTOS
     * @return
     */
    List<BaseApiResourceDTO> addApiResources(List<BaseApiResource2CreateDTO> createDTOS);
}
