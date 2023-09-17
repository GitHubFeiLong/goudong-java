package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;

import java.util.List;

/**
 * Service Interface for managing {@link BaseMenu}.
 */
public interface BaseMenuService {

    /**
     * 查询应用下所有菜单
     * @param appId 应用id
     * @return 菜单集合
     */
    List<BaseMenuDTO> findAllByAppId(Long appId);

    /**
     * 查询菜单
     * @param ids 菜单id集合
     * @return 菜单集合
     */
    List<BaseMenu> findAllById(List<Long> ids);
    // ==shanchu


}
