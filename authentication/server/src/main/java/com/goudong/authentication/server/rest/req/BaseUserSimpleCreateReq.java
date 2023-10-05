package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

/**
 * 新增用户
 */
@Data
public class BaseUserSimpleCreateReq implements Serializable {


    /**
     * 用户名
     */
    @NotNull
    @Size(max = 16)
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 密码
     */
    @NotNull
    @Size(max = 32)
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    /**
     * 密码
     */
    @NotNull
    @NotEmpty
    @ApiModelProperty(value = "角色id", required = true)
    private List<Long> roleIds;

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;
}
