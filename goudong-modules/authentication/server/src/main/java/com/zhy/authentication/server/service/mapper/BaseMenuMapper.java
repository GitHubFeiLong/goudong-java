package com.zhy.authentication.server.service.mapper;

import com.zhy.authentication.server.domain.BaseMenu;
import com.zhy.authentication.server.service.dto.BaseMenuDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BaseMenu} and its DTO {@link BaseMenuDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseMenuMapper extends EntityMapper<BaseMenuDTO, BaseMenu> {


    @Mapping(target = "roles", ignore = true)
    BaseMenu toEntity(BaseMenuDTO baseMenuDTO);

    default BaseMenu fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseMenu baseMenu = new BaseMenu();
        baseMenu.setId(id);
        return baseMenu;
    }
}
