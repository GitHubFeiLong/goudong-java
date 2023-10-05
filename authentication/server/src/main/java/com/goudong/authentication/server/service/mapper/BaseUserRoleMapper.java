package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseUserRole;
import com.goudong.authentication.server.service.dto.BaseUserRoleDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link BaseUserRole} and its DTO {@link BaseUserRoleDTO}.
 */
@Mapper(componentModel = "spring", uses = {BaseUserMapper.class, BaseRoleMapper.class})
public interface BaseUserRoleMapper extends EntityMapper<BaseUserRoleDTO, BaseUserRole> {

    // @Mapping(source = "user.id", target = "userId")
    // @Mapping(source = "role.id", target = "roleId")
    BaseUserRoleDTO toDto(BaseUserRole baseUserRole);

    // @Mapping(source = "userId", target = "user")
    // @Mapping(source = "roleId", target = "role")
    BaseUserRole toEntity(BaseUserRoleDTO baseUserRoleDTO);

    default BaseUserRole fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseUserRole baseUserRole = new BaseUserRole();
        baseUserRole.setId(id);
        return baseUserRole;
    }
}
