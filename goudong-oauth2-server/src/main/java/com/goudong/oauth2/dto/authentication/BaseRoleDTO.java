package com.goudong.oauth2.dto.authentication;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 角色
 * @author cfl
 * @date 2023/6/17 10:28
 * @version 1.0
 */
@Data
public class BaseRoleDTO {

    @ApiModelProperty("角色id")
    private Long id;

    @ApiModelProperty("角色名称(必须以ROLE_起始命名)")
    private String roleName;

    @ApiModelProperty("角色名称中文")
    private String roleNameCn;

    public BaseRoleDTO() {
    }

    public BaseRoleDTO(Long id, String roleName, String roleNameCn) {
        this.id = id;
        this.roleName = roleName;
        this.roleNameCn = roleNameCn;
    }
}
