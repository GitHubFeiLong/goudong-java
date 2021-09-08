package com.goudong.oauth2.mapstruct.po;

import lombok.Data;

import java.util.List;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/9/8 23:57
 */
@Data
public class MenuDO {
    private String menuName;

    private String url;

    private List<MenuDO> subMenus;
}
