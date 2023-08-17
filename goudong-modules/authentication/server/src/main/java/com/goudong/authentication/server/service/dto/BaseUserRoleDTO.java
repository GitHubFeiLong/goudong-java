package com.goudong.authentication.server.service.dto;

import com.goudong.authentication.server.domain.BaseUserRole;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * A DTO for the {@link BaseUserRole} entity.
 */
@ApiModel(description = "用户角色中间表")
@Data
public class BaseUserRoleDTO implements Serializable {

    private Long id;


    private Long userId;

    private Long roleId;
}
