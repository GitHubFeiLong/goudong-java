package com.zhy.authentication.server.service.mapper;

import com.zhy.authentication.server.domain.BaseApp;
import com.zhy.authentication.server.service.dto.BaseAppDTO;
import org.mapstruct.*;

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
