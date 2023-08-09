package com.zhy.authentication.server.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.zhy.authentication.server.domain.BaseRoleMenu} entity.
 */
@ApiModel(description = "菜单角色中间表")
@Data
public class BaseRoleMenuDTO implements Serializable {

    private Long id;


    private Long roleId;

    private Long menuId;

}
