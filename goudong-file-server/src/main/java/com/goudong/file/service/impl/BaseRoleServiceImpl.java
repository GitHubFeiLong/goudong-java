package com.goudong.file.service.impl;

import com.goudong.boot.redis.core.RedisTool;
import com.goudong.core.util.CollectionUtil;
import com.goudong.core.util.MessageFormatUtil;
import com.goudong.file.enumerate.RedisKeyProviderEnum;
import com.goudong.file.po.user.BaseRolePO;
import com.goudong.file.repository.user.BaseRoleRepository;
import com.goudong.file.service.BaseRoleService;
import com.goudong.file.service.IExcelDataValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/11/23 13:10
 */
@Service
@RequiredArgsConstructor
public class BaseRoleServiceImpl implements BaseRoleService, IExcelDataValidation {

    //~fields
    //==================================================================================================================
    /**
     * 用户持久层接口
     */
    private final BaseRoleRepository baseRoleRepository;

    private final RedisTool redisTool;

    //~methods
    //==================================================================================================================
    /**
     * 动态获取数据校验的下拉数据
     *
     * @return
     */
    @Override
    public String[] get() {
        List<BaseRolePO> all = baseRoleRepository.findAll();
        String[] roleNameCns = all.stream().map(BaseRolePO::getRoleNameCn).toArray(String[]::new);
        return roleNameCns;
    }

    /**
     * 查询所有角色
     *
     * @return
     */
    @Override
    public List<BaseRolePO> findAll() {
        List<BaseRolePO> list = redisTool.getList(RedisKeyProviderEnum.ROLE, BaseRolePO.class);
        if (CollectionUtil.isEmpty(list)) {
            synchronized (this) {
                list = redisTool.getList(RedisKeyProviderEnum.ROLE, BaseRolePO.class);
                if (CollectionUtil.isEmpty(list)) {
                    list = baseRoleRepository.findAll();
                    list.stream().forEach(p->p.setUsers(new ArrayList<>()));
                    list.stream().forEach(p->p.setMenus(new ArrayList<>()));
                    redisTool.set(RedisKeyProviderEnum.ROLE, list);
                }
            }
        }
        return list;
    }

    /**
     * 根据角色中文名在缓存中查询对象
     *
     * @param roleNameCn 角色中文名
     * @return
     */
    @Override
    public BaseRolePO findByRoleNameCnInCatch(String roleNameCn) {
        return findAll().stream()
                .filter(f -> Objects.equals(f.getRoleNameCn(), roleNameCn))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(MessageFormatUtil.format("角色{}不存在", roleNameCn)));
    }
}
