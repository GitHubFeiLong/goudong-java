package com.goudong.oauth2.service;


import com.goudong.commons.dto.oauth2.HideMenu2CreateDTO;
import com.goudong.oauth2.dto.authentication.BaseMenuDTO;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 类描述：
 * 菜单接口
 * @author msi
 * @date 2022/1/23 9:12
 * @version 1.0
 */
public interface BaseMenuService {

    /**
     * 查询所有菜单
     * @return
     */
    List<BaseMenuDTO> findAll();

    /**
     * 查询指定role的菜单资源
     * @param role
     * @return
     */
    List<BaseMenuDTO> findAllByRole(@NotBlank String role);

    /**
     * 保存隐藏菜单
     * @param createDTOS
     * @return
     */
    List<BaseMenuDTO> addHideMenu(List<HideMenu2CreateDTO> createDTOS);
}

