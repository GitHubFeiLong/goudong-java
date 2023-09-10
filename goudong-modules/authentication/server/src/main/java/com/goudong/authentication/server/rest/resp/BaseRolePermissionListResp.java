package com.goudong.authentication.server.rest.resp;

import com.goudong.authentication.server.service.dto.BaseMenuDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseRolePermissionListResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "角色名称")
    private String name;

    @ApiModelProperty(value = "备注")
    private String remark;

    @ApiModelProperty(value = "角色能设置的权限")
    private List<BaseMenuDTO> permission;

    //~methods
    //==================================================================================================================
}
