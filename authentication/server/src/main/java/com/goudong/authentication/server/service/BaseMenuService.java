package com.goudong.authentication.server.service;

import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.rest.req.BaseMenuChangeSortNumReq;
import com.goudong.authentication.server.rest.req.BaseMenuCreateReq;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.req.BaseMenuUpdateReq;
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
     * @param id 菜单id
     * @return 菜单
     */
    BaseMenu findById(Long id);

    /**
     * 查询菜单
     * @param ids 菜单id集合
     * @return 菜单集合
     */
    List<BaseMenu> findAllById(List<Long> ids);

    /**
     * 查询指定条件下的菜单
     * @param req 条件
     * @return 菜单集合
     */
    @Deprecated
    List<BaseMenuDTO> findAll(BaseMenuGetAllReq req);

    /**
     * 新增菜单
     * @param req
     * @return
     */
    BaseMenuDTO save(BaseMenuCreateReq req);

    /**
     * 更新菜单
     * @param req
     * @return
     */
    BaseMenuDTO update(BaseMenuUpdateReq req);

    /**
     * 删除菜单，如果菜单是父节点，就会删除它及它下面的所有子节点
     *
     * @param id
     * @return
     */
    Boolean deleteById(Long id);

    /**
     * 修改菜单排序
     * @param req
     * @return
     */
    Boolean changeSortNum(BaseMenuChangeSortNumReq req);
    // ==shanchu


}
