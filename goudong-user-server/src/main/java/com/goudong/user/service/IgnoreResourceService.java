package com.goudong.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.po.BaseIgnoreResourcePO;

import java.util.List;

/**
 * 接口描述：
 * 白名单服务层接口
 * @Author msi
 * @Date 2021-08-14 11:50
 * @Version 1.0
 */
public interface IgnoreResourceService extends IService<BaseIgnoreResourcePO> {
    /**
     * 集合添加到白名单
     * @param insertDTOList
     * @return
     */
    List<BaseIgnoreResourceDTO> addList(List<BaseIgnoreResourceDTO> insertDTOList);
}
