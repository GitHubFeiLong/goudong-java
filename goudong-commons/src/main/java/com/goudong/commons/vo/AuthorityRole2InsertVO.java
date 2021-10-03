package com.goudong.commons.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * 新增角色
 * @Author msi
 * @Date 2021-08-19 20:42
 * @Version 1.0
 */
@Data
@ApiModel
public class AuthorityRole2InsertVO {
    /**
     * 角色名称(必须以ROLE_起始命名)
     */
    @NotBlank(message = "roleName不能为空")
    @ApiModelProperty(value = "角色英文名", required = true)
    private String roleName;

    /**
     * 角色名称中文
     */
    @NotBlank(message = "roleNameCn不能为空")
    @ApiModelProperty(value = "角色中文名", required = true)
    private String roleNameCn;

    /**
     * 备注
     */
    private String remark;
}