package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 新增角色
 */
@Data
public class BaseRoleUpdateReq implements Serializable {

    /**
     * id
     */
    @NotNull
    @ApiModelProperty(value = "id", required = true)
    private Long id;

    @NotBlank
    @Size(min = 4, max = 16)
    @ApiModelProperty(value = "角色名称", required = true)
    private String name;

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;
}
