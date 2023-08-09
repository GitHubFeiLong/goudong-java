package com.zhy.authentication.server.service.mapper;

import com.zhy.authentication.server.domain.BaseRole;
import com.zhy.authentication.server.service.dto.BaseRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BaseRole} and its DTO {@link BaseRoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseRoleMapper extends EntityMapper<BaseRoleDTO, BaseRole> {


    @Mapping(target = "users", ignore = true)
    @Mapping(target = "menus", ignore = true)
    BaseRole toEntity(BaseRoleDTO baseRoleDTO);

    default BaseRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseRole baseRole = new BaseRole();
        baseRole.setId(id);
        return baseRole;
    }
}
