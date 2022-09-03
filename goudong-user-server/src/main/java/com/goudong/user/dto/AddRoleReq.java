package com.goudong.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * 类描述：
 * 新增角色
 * @author cfl
 * @version 1.0
 * @date 2022/9/3 14:36
 */
@Data
public class AddRoleReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "角色名", required = true)
    @NotEmpty
    private String roleNameCn;

    @ApiModelProperty(value = "备注")
    private String remark;
    //~methods
    //==================================================================================================================
}
