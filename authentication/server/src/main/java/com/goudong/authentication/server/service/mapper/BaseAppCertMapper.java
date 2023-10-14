package com.goudong.authentication.server.service.mapper;

import com.goudong.authentication.server.domain.BaseAppCert;
import com.goudong.authentication.server.service.dto.BaseAppCertDTO;
import org.mapstruct.Mapper;

/**
 * Mapper for the entity {@link BaseAppCert} and its DTO {@link BaseAppCertDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface BaseAppCertMapper extends EntityMapper<BaseAppCertDTO, BaseAppCert>  {
}
