package com.goudong.authentication.server.service.manager.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.authentication.server.domain.BaseMenu;
import com.goudong.authentication.server.domain.BaseRole;
import com.goudong.authentication.server.rest.req.BaseRoleChangePermissionReq;
import com.goudong.authentication.server.rest.req.BaseRoleCreateReq;
import com.goudong.authentication.server.rest.req.BaseRolePageReq;
import com.goudong.authentication.server.rest.req.BaseRoleUpdateReq;
import com.goudong.authentication.server.rest.req.search.BaseRoleDropDownReq;
import com.goudong.authentication.server.rest.resp.BaseRolePermissionListResp;
import com.goudong.authentication.server.rest.resp.BaseRoleDropDownResp;
import com.goudong.authentication.server.rest.resp.BaseRolePageResp;
import com.goudong.authentication.server.service.BaseMenuService;
import com.goudong.authentication.server.service.BaseRoleService;
import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import com.goudong.authentication.server.service.dto.BaseRoleDTO;
import com.goudong.authentication.server.service.dto.MyAuthentication;
import com.goudong.authentication.server.service.manager.BaseRoleManagerService;
import com.goudong.authentication.server.util.SecurityContextUtil;
import com.goudong.boot.web.core.ClientException;
import com.goudong.core.lang.PageResult;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.tree.v2.Tree;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 角色管理服务层接口实现类
 * @author cfl
 * @version 1.0
 */
@Service
public class BaseRoleManagerServiceImpl implements BaseRoleManagerService {
    //~fields
    //==================================================================================================================
    @Resource
    private BaseRoleService baseRoleService;

    @Resource
    private BaseMenuService baseMenuService;

    //~methods
    //==================================================================================================================
    /**
     * 用户所在应用下的角色下拉
     *
     * @param req 条件查询参数
     * @return 角色下拉分页对象
     */
    @Override
    public PageResult<BaseRoleDropDownResp> roleDropDown(BaseRoleDropDownReq req) {
        return baseRoleService.roleDropDown(req);
    }

    /**
     * 分页查询角色列表
     *
     * @param req 条件查询参数
     * @return 角色分页列表
     */
    @Override
    public PageResult<BaseRolePageResp> page(BaseRolePageReq req) {
        return baseRoleService.page(req);
    }

    /**
     * 保存角色
     *
     * @param req 角色信息
     * @return 保存后对象
     */
    @Override
    public BaseRoleDTO save(BaseRoleCreateReq req) {
        return baseRoleService.save(req);
    }

    /**
     * 修改角色
     *
     * @param req 需要修改的角色信息
     * @return 修改后角色信息
     */
    @Override
    public BaseRoleDTO update(BaseRoleUpdateReq req) {
        return baseRoleService.update(req);
    }

    /**
     * 批量删除角色
     *
     * @param ids 删除的id集合
     * @return true删除成功；false删除失败
     */
    @Override
    public Boolean deleteByIds(List<Long> ids) {
        return baseRoleService.deleteByIds(ids);
    }

    /**
     * 查询角色id对应的权限信息
     *
     * @param id 角色id
     * @return 角色信息和权限信息
     */
    @Override
    @Transactional(readOnly = true)
    public BaseRolePermissionListResp getPermissionListById(Long id) {
        BaseRole rolePO = baseRoleService.findById(id);
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        AssertUtil.isEquals(rolePO.getAppId(), myAuthentication.getRealAppId(), () -> ClientException.clientByForbidden());
        // 当前用户所拥有的菜单权限，不能越级设置权限

        List<BaseMenuDTO> permissions;

        BaseRolePermissionListResp resp = BeanUtil.copyProperties(rolePO, BaseRolePermissionListResp.class);
        // ADMIN 直接返回所有
        if (myAuthentication.admin()) {
            permissions = baseMenuService.findAllByAppId(myAuthentication.getRealAppId());
        } else {
            // 查询角色对应菜单，并去重
            permissions = baseRoleService.listPermissionsByLoginUser();
        }

        List<Long> menuIds = rolePO.getMenus().stream().map(BaseMenu::getId).collect(Collectors.toList());
        // 拥有的权限
        permissions.stream().forEach(p -> {
            if (menuIds.contains(p.getId())) {
                p.setChecked(true);
            }
        });

        // 转换成Tree
        resp.setPermission(Tree.getInstance().toTree(permissions));

        return resp;
    }

    /**
     * 修改角色权限
     *
     * @param req 被修改的角色和需要设置的权限信息
     * @return true修改成功；false修改失败
     */
    @Override
    @Transactional
    public Boolean changePermission(BaseRoleChangePermissionReq req) {
        BaseRole rolePO = baseRoleService.findById(req.getId());
        MyAuthentication myAuthentication = SecurityContextUtil.get();
        AssertUtil.isEquals(rolePO.getAppId(), myAuthentication.getRealAppId(), () -> ClientException.clientByForbidden());
        if (CollectionUtil.isEmpty(req.getMenuIds())) {
            rolePO.setMenus(new ArrayList<>(0));
            return true;
        }

        List<BaseMenu> menus = baseMenuService.findAllById(req.getMenuIds());
        menus.forEach(menu -> AssertUtil.isEquals(menu.getAppId(), myAuthentication.getRealAppId(), () -> ClientException.clientByForbidden()));

        rolePO.setMenus(menus);
        return true;
    }

}
