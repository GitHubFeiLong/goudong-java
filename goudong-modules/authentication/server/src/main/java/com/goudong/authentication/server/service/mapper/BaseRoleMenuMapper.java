package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseRoleMenu;
import com.goudong.authentication.server.service.dto.BaseRoleMenuDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BaseRoleMenu} and its DTO {@link BaseRoleMenuDTO}.
 */
@Mapper(componentModel = "spring", uses = {BaseRoleMapper.class, BaseMenuMapper.class})
public interface BaseRoleMenuMapper extends EntityMapper<BaseRoleMenuDTO, BaseRoleMenu> {

    // @Mapping(source = "role.id", target = "roleId")
    // @Mapping(source = "menu.id", target = "menuId")
    BaseRoleMenuDTO toDto(BaseRoleMenu baseRoleMenu);

    // @Mapping(source = "roleId", target = "role")
    // @Mapping(source = "menuId", target = "menu")
    BaseRoleMenu toEntity(BaseRoleMenuDTO baseRoleMenuDTO);

    default BaseRoleMenu fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseRoleMenu baseRoleMenu = new BaseRoleMenu();
        baseRoleMenu.setId(id);
        return baseRoleMenu;
    }
}
