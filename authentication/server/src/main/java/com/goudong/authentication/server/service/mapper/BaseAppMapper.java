package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseApp;
import com.goudong.authentication.server.service.dto.BaseAppDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link BaseApp} and its DTO {@link BaseAppDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseAppMapper extends EntityMapper<BaseAppDTO, BaseApp> {



    default BaseApp fromId(Long id) {
        if (id == null) {
            return null;
        }
        BaseApp baseApp = new BaseApp();
        baseApp.setId(id);
        return baseApp;
    }
}
