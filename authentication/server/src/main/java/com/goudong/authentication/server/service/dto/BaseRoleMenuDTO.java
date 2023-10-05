package com.goudong.authentication.server.service.dto;

import com.goudong.authentication.server.domain.BaseRoleMenu;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link BaseRoleMenu} entity.
 */
@ApiModel(description = "菜单角色中间表")
@Data
public class BaseRoleMenuDTO implements Serializable {

    private Long id;


    private Long roleId;

    private Long menuId;

}
