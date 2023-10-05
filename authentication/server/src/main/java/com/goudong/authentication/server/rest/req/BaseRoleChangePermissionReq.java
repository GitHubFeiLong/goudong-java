package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：
 * 修改角色权限
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseRoleChangePermissionReq {
    //~fields
    //==================================================================================================================
    @NotNull
    @ApiModelProperty(value = "角色id", required = true)
    private Long id;

    @NotNull
    @ApiModelProperty(value = "菜单id集合", required = true)
    private List<Long> menuIds;
    //~methods
    //==================================================================================================================
}
