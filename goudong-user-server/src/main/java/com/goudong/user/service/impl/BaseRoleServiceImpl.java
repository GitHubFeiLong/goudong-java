package com.goudong.user.service.impl;

import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.enumerate.ServerExceptionEnum;
import com.goudong.commons.exception.user.RoleException;
import com.goudong.user.po.BaseRolePO;
import com.goudong.user.repository.BaseRoleRepository;
import com.goudong.user.service.BaseRoleService;
import org.springframework.stereotype.Service;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/1/7 15:34
 */
@Service
public class BaseRoleServiceImpl implements BaseRoleService {
    private final BaseRoleRepository baseRoleRepository;

    public BaseRoleServiceImpl(BaseRoleRepository baseRoleRepository) {
        this.baseRoleRepository = baseRoleRepository;
    }

    /**
     * 查询预置的普通角色
     *
     * @return
     */
    @Override
    public BaseRolePO findByRoleOrdinary() {
        String serverMessage = String.format("预置的角色：%s不存在", RoleConst.ROLE_ORDINARY);
        return baseRoleRepository.findByRoleName(RoleConst.ROLE_ORDINARY)
                .orElseThrow(()-> new RoleException(ServerExceptionEnum.SERVER_ERROR, serverMessage));
    }
}
