package com.goudong.user.service;

import com.goudong.user.dto.*;

import java.util.List;

/**
 * 接口描述：
 * 菜单
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
public interface BaseMenuService {

    /**
     * 查询系统所有菜单
     * @return
     */
    List<BaseMenuDTO> findAll();
    /**
     * 初始化
     * @param req
     * @return
     */
    List<BaseMenuDTO> init(List<InitMenuReq> req);

    /**
     * 查询角色的多有菜单
     * @param role
     * @return
     */
    List<BaseMenuDTO> findAllByRoleName(String role);

    /**
     * 根据角色名查询进行去重后的所有菜单
     * @param roleNames
     * @return
     */
    List<BaseMenuDTO> findAllByRoleNames(List<String> roleNames);

    /**
     * 分页查询菜单
     * @param req
     * @return
     */
    List<BaseMenuDTO> listByTree(BaseMenuPageReq req);

    /**
     * 新增菜单
     * @param req
     * @return
     */
    BaseMenuDTO addMenu(AddMenuReq req);

    /**
     * 修改菜单
     * @param req
     * @return
     */
    BaseMenuDTO updateMenu(UpdateMenuReq req);
}
