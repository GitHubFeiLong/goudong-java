package com.goudong.oauth2.mapstruct.po;

import lombok.Data;

import java.util.List;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/9/8 23:58
 */
@Data
public class MenuVO {
    private String name;

    private String path;

    private List<MenuVO> child;
}
