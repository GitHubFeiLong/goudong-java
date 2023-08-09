package com.zhy.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 新增角色
 */
@Data
public class BaseRoleUpdate implements Serializable {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;
}
