package com.zhy.authentication.server.service.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.zhy.authentication.server.domain.BaseUserRole} entity.
 */
@ApiModel(description = "用户角色中间表")
@Data
public class BaseUserRoleDTO implements Serializable {

    private Long id;


    private Long userId;

    private Long roleId;
}
