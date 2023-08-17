package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.validation.AppValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 新增角色
 */
@Data
public class BaseRoleCreate implements Serializable {

    /**
     * 应用id
     */
    @NotNull
    @ApiModelProperty(value = "应用id", required = true)
    @AppValidator
    private Long appId;

    /**
     * 名称
     */
    @NotNull
    @Size(max = 16)
    @ApiModelProperty(value = "名称", required = true)
    private String name;

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;
}
