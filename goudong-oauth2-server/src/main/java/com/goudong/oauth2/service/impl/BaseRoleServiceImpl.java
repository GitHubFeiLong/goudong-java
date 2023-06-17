package com.goudong.oauth2.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.oauth2.dto.authentication.BaseMenuDTO;
import com.goudong.oauth2.dto.authentication.BaseRoleDTO;
import com.goudong.oauth2.dto.authentication.BaseUserDTO;
import com.goudong.oauth2.po.BaseMenuPO;
import com.goudong.oauth2.po.BaseRolePO;
import com.goudong.oauth2.repository.BaseRoleRepository;
import com.goudong.oauth2.service.BaseRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
     * 填充角色和菜单
     *
     * @param ids
     * @param user
     * @return
     */
    @Override
    @Transactional
    public void fillRoleAndMenu(List<Long> ids, BaseUserDTO user) {
        List<BaseRolePO> allById = baseRoleRepository.findAllById(ids);
        List<BaseMenuPO> menuPOS = allById.stream().flatMap(m -> m.getMenus().stream()).collect(Collectors.toList());
        Map<Long, BaseMenuDTO> menuDTOMap = new HashMap();
        menuPOS.stream().forEach(p -> {
            if (!menuDTOMap.containsKey(p.getId())) {
                menuDTOMap.put(p.getId(), BeanUtil.copyProperties(p, BaseMenuDTO.class));
            }
        });

        List<BaseMenuDTO> baseMenuDTOS = menuDTOMap.values().stream().sorted().collect(Collectors.toList());
        List<BaseRoleDTO> baseRoleDTOS = BeanUtil.copyToList(allById, BaseRoleDTO.class);
        user.setRoles(baseRoleDTOS);
        user.setMenus(baseMenuDTOS);
    }
}
