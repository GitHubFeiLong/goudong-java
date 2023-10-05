package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.BaseMenuChangeSortNumReq;
import com.goudong.authentication.server.rest.req.BaseMenuCreateReq;
import com.goudong.authentication.server.rest.req.BaseMenuGetAllReq;
import com.goudong.authentication.server.rest.req.BaseMenuUpdateReq;
import com.goudong.authentication.server.rest.resp.BaseMenuGetAllResp;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;

/**
 * 类描述：
 * 应用管理服务接口
 * @author cfl
 * @version 1.0
 */
public interface BaseMenuManagerService {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================

    /**
     * 查询所有菜单
     * @param req 查询条件
     * @return 树形结构的菜单
     */
    BaseMenuGetAllResp getAll(BaseMenuGetAllReq req);

    /**
     * 新增菜单
     * @param req 菜单参数
     * @return 新增后菜单
     */
    BaseMenuDTO save(BaseMenuCreateReq req);

    /**
     * 更新菜单
     * @param req 被修改菜单
     * @return 修改后菜单
     */
    BaseMenuDTO update(BaseMenuUpdateReq req);

    /**
     * 删除菜单，如果菜单是父节点，就会删除它及它下面的所有子节点
     * @param id
     * @return
     */
    Boolean deleteById(Long id);

    /**
     * 修改排序
     * @param req
     * @return
     */
    Boolean changeSortNum(BaseMenuChangeSortNumReq req);
}
