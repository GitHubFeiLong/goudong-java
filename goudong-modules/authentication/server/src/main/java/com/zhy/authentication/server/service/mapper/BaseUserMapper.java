package com.zhy.authentication.server.service.mapper;

import com.zhy.authentication.server.domain.BaseUser;
import com.zhy.authentication.server.service.dto.BaseUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BaseUser} and its DTO {@link BaseUserDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseUserMapper extends EntityMapper<BaseUserDTO, BaseUser> {


    @Mapping(target = "roles", ignore = true)
    BaseUser toEntity(BaseUserDTO baseUserDTO);

    default BaseUser fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseUser baseUser = new BaseUser();
        baseUser.setId(id);
        return baseUser;
    }
}
