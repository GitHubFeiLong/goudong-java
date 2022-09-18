package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.copier.CopyOptions;
import com.goudong.commons.dto.oauth2.BaseRoleDTO;
import com.goudong.commons.utils.core.BeanUtil;
import com.goudong.oauth2.po.BaseRolePO;
import com.goudong.oauth2.repository.BaseRoleRepository;
import com.goudong.oauth2.service.BaseRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 类描述：
 * 角色服务层实现
 * @author cfl
 * @version 1.0
 * @date 2022/9/17 21:48
 */
@Service
@RequiredArgsConstructor
public class BaseRoleServiceImpl implements BaseRoleService {

    //~fields
    //==================================================================================================================
    private final BaseRoleRepository baseRoleRepository;

    //~methods
    //==================================================================================================================
    /**
     * 查询角色集合
     *
     * @param ids
     * @return
     */
    @Transactional
    @Override
    public List<BaseRoleDTO> listByIds(List<Long> ids) {
        List<BaseRolePO> allById = baseRoleRepository.findAllById(ids);

        // allById.stream().forEach(p->p.getMenus());
        List<BaseRoleDTO> baseRoleDTOS = BeanUtil.copyToList(allById, BaseRoleDTO.class, CopyOptions.create());
        return baseRoleDTOS;
    }

}
