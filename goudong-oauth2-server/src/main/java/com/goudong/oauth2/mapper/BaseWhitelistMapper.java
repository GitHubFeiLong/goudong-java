package com.goudong.oauth2.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.commons.dto.oauth2.BaseWhitelist2CreateDTO;
import com.goudong.oauth2.po.BaseWhitelistPO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/9 2:49
 */
@Mapper(componentModel = "spring")
public interface BaseWhitelistMapper {

    BaseWhitelistMapper INSTANCE = Mappers.getMapper(BaseWhitelistMapper.class);

    default BaseWhitelistPO createDTO2PO(BaseWhitelist2CreateDTO createDTO) throws JsonProcessingException {
        if (createDTO == null) {
            return null;
        }
        BaseWhitelistPO baseWhitelistPO = new BaseWhitelistPO();
        baseWhitelistPO.setPattern(createDTO.getPattern());
        String methods = new ObjectMapper().writeValueAsString(createDTO.getMethods());
        baseWhitelistPO.setMethods(methods);
        baseWhitelistPO.setRemark(createDTO.getRemark());
        baseWhitelistPO.setIsSystem(createDTO.getIsSystem());
        baseWhitelistPO.setIsInner(createDTO.getIsInner());
        baseWhitelistPO.setIsDisable(createDTO.getIsDisable());
        baseWhitelistPO.setDeleted(false);

        return baseWhitelistPO;
    };

    /**
     * 保存白名单的集合转换
     * @param createDTOS
     * @return
     */
    List<BaseWhitelistPO> createDTOS2POS(List<BaseWhitelist2CreateDTO> createDTOS);

}
