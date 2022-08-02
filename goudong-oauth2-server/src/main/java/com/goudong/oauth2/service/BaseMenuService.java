package com.goudong.oauth2.service;



import com.goudong.commons.dto.oauth2.BaseMenuDTO;

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
     * 查询所有菜单，返回Tree
     * @return
     */
    List<BaseMenuDTO> findAll2Tree();

    /**
     * 查询指定role的菜单资源
     * @param role
     * @return
     */
    List<BaseMenuDTO> findAllByRole(@NotBlank String role);
}

