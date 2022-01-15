package com.goudong.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.goudong.commons.dto.BaseIgnoreResourceDTO;
import com.goudong.commons.po.BaseIgnoreResourcePO;

import java.util.List;

/**
 * 接口描述：
 * 白名单服务层接口
 *
 * @deprecated 重构项目后，将这个去掉使用新的白名单{@link com.goudong.commons.annotation.core.Whitelist}
 * @Author msi
 * @Date 2021-08-14 11:50
 * @Version 1.0
 */
@Deprecated
public interface CommonsIgnoreResourceService extends IService<BaseIgnoreResourcePO> {
    /**
     * 集合添加到白名单
     * @param insertDTOList
     * @return
     */
    List<BaseIgnoreResourceDTO> addList(List<BaseIgnoreResourceDTO> insertDTOList);
}
