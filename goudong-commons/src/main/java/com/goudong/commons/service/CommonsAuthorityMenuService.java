package com.goudong.commons.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.po.AuthorityMenuPO;

import java.util.List;

/**
 * 菜单服务层接口
 */
public interface CommonsAuthorityMenuService extends IService<AuthorityMenuPO> {

    /**
     * 批量添加菜单
     * @param insetDTOS
     * @return
     */
    List<AuthorityMenuDTO> addList(List<AuthorityMenuDTO> insetDTOS);
}
