package com.goudong.authentication.server.service.manager;

import com.goudong.authentication.server.rest.req.BaseRoleChangePermissionReq;
import com.goudong.authentication.server.rest.req.BaseRoleCreateReq;
import com.goudong.authentication.server.rest.req.BaseRolePageReq;
import com.goudong.authentication.server.rest.req.BaseRoleUpdateReq;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseRolePermissionListResp;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseRolePageResp;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.core.lang.PageResult;

import java.util.List;

/**
 * 类描述：
 * 角色管理服务层接口
 * @author cfl
 * @version 1.0
 */
public interface BaseRoleManagerService {

    /**
     * 用户所在应用下的角色下拉
     * @param req 条件查询参数
     * @return 角色下拉分页对象
     */
    PageResult<BaseRoleDropDownResp> roleDropDown(BaseRoleDropDownReq req);

    /**
     * 分页查询角色列表
     * @param req 条件查询参数
     * @return 角色分页列表
     */
    PageResult<BaseRolePageResp> page(BaseRolePageReq req);

    /**
     * 保存角色
     * @param req 角色信息
     * @return 保存后对象
     */
    BaseRoleDTO save(BaseRoleCreateReq req);

    /**
     * 修改角色
     * @param req 需要修改的角色信息
     * @return 修改后角色信息
     */
    BaseRoleDTO update(BaseRoleUpdateReq req);

    /**
     * 批量删除角色
     * @param ids 删除的id集合
     * @return true删除成功；false删除失败
     */
    Boolean deleteByIds(List<Long> ids);

    /**
     * 查询角色id对应的权限列表
     * @param id 角色id
     * @return 角色信息和权限信息
     */
    BaseRolePermissionListResp getPermissionListById(Long id);

    /**
     * 修改角色权限
     * @param req 被修改的角色和需要设置的权限信息
     * @return true修改成功；false修改失败
     */
    Boolean changePermission(BaseRoleChangePermissionReq req);
}
