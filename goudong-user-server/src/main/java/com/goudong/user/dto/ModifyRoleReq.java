package com.goudong.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 新增角色
 * @author cfl
 * @version 1.0
 * @date 2022/9/3 14:36
 */
@Data
public class ModifyRoleReq {
    //~fields
    //==================================================================================================================
    @NotNull
    @ApiModelProperty(value = "角色id", required = true)
    private Long id;

    @ApiModelProperty(value = "备注")
    private String remark;
    //~methods
    //==================================================================================================================
}
