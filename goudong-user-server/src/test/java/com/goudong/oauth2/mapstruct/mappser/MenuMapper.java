package com.goudong.oauth2.mapstruct.mappser;

import com.goudong.oauth2.mapstruct.po.MenuDO;
import com.goudong.oauth2.mapstruct.po.MenuVO;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * 接口描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/9/9 0:00
 */
@Mapper(collectionMappingStrategy= CollectionMappingStrategy.SETTER_PREFERRED)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);
    @Mappings({
            @Mapping(source = "menuName", target = "name"),
            @Mapping(source = "url", target = "path"),
            @Mapping(source = "subMenus", target = "child"),
    })
    MenuVO do2Vo(MenuDO person);

    // List<MenuVO> do2vo(List<MenuDO> people);
}
