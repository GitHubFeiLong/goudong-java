package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.framework.redis.RedisTool;
import com.goudong.commons.tree.v2.Tree;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.oauth2.dto.BaseMenuDTO2Redis;
import com.goudong.oauth2.enumerate.RedisKeyProviderEnum;
import com.goudong.oauth2.po.BaseMenuPO;
import com.goudong.oauth2.po.BaseRolePO;
import com.goudong.oauth2.repository.BaseMenuRepository;
import com.goudong.oauth2.repository.BaseRoleRepository;
import com.goudong.oauth2.service.BaseMenuService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 菜单接口的实现类
 * @author msi
 * @version 1.0
 * @date 2022/1/23 9:12
 */
@Service
@RequiredArgsConstructor
public class BaseMenuServiceImpl implements BaseMenuService {

    //~fields
    //==================================================================================================================
    /**
     * 菜单持久层
     */
    private final BaseMenuRepository baseMenuRepository;

    /**
     * 角色持久层
     */
    private final BaseRoleRepository baseRoleRepository;

    private final RedisTool redisTool;
    //~methods
    //==================================================================================================================

    /**
     * 查询所有菜单，返回Tree
     *
     * @return
     */
    @Override
    public List<BaseMenuDTO> findAll2Tree() {
        // 查询redis是否存在，不存在再加锁查询数据库
        List<BaseMenuDTO2Redis> menuDTO2Redis = redisTool.getList(RedisKeyProviderEnum.MENU_ALL, BaseMenuDTO2Redis.class);
        if (CollectionUtils.isNotEmpty(menuDTO2Redis)) {
            return BeanUtil.copyToList(menuDTO2Redis, BaseMenuDTO.class, CopyOptions.create());
        }
        synchronized (this) {
            menuDTO2Redis = redisTool.getList(RedisKeyProviderEnum.MENU_ALL, BaseMenuDTO2Redis.class);
            if (CollectionUtils.isNotEmpty(menuDTO2Redis)) {
                return BeanUtil.copyToList(menuDTO2Redis, BaseMenuDTO.class, CopyOptions.create());
            }
            // 查询数据库
            List<BaseMenuPO> menus = baseMenuRepository.findAll();
            if (CollectionUtils.isNotEmpty(menus)) {
                menuDTO2Redis = BeanUtil.copyToList(menus, BaseMenuDTO2Redis.class, CopyOptions.create());
                // 获取Tree
                List<BaseMenuDTO2Redis> menuTree = Tree.getInstance().id(BaseMenuDTO2Redis::getId)
                        .parentId(BaseMenuDTO2Redis::getParentId)
                        .children(BaseMenuDTO2Redis::getChildren)
                        .toTree(menuDTO2Redis);

                // TODO 需要将其直接转换成前端需要格式，后面读取缓存后直接返回前端，不用额外处理格式等
                redisTool.set(RedisKeyProviderEnum.MENU_ALL, menuTree);

                return BeanUtil.copyToList(menuTree, BaseMenuDTO.class, CopyOptions.create());
            }
        }

        return new ArrayList<>(0);
    }

    /**
     * 查询指定role的菜单资源
     *
     * @param role
     * @return
     */
    @Override
    @Transactional
    public List<BaseMenuDTO> findAllByRole(final String role) {
        // 匿名角色，不需要查询权限
        if(RoleConst.ROLE_ANONYMOUS.equals(role)) {
            return new ArrayList<>(0);
        }
        // 查询redis是否存在，不存在再加锁查询数据库
        List<BaseMenuDTO2Redis> menuDTO2Redis = redisTool.getList(RedisKeyProviderEnum.MENU_ROLE, BaseMenuDTO2Redis.class, role);
        if (CollectionUtils.isNotEmpty(menuDTO2Redis)) {
            return BeanUtil.copyToList(menuDTO2Redis, BaseMenuDTO.class, CopyOptions.create());
        }
        synchronized (this) {
            menuDTO2Redis = redisTool.getList(RedisKeyProviderEnum.MENU_ROLE, BaseMenuDTO2Redis.class, role);
            if (CollectionUtils.isNotEmpty(menuDTO2Redis)) {
                return BeanUtil.copyToList(menuDTO2Redis, BaseMenuDTO.class, CopyOptions.create());
            }
            // 查询数据库
            BaseRolePO baseRolePO = baseRoleRepository.findByRoleName(role)
                    .orElseThrow(() -> ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, "参数错误，角色不存在"));
            List<BaseMenuPO> menus = baseRolePO.getMenus();

            if (CollectionUtils.isNotEmpty(menus)) {
                menuDTO2Redis = BeanUtil.copyToList(menus, BaseMenuDTO2Redis.class, CopyOptions.create());

                redisTool.set(RedisKeyProviderEnum.MENU_ROLE, menuDTO2Redis, role);

                return BeanUtil.copyToList(menuDTO2Redis, BaseMenuDTO.class, CopyOptions.create());
            }
        }

        return new ArrayList<>(0);
    }
}