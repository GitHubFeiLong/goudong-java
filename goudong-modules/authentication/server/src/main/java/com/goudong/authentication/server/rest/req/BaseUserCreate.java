package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.validation.AppValidator;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 新增用户
 */
@Data
public class BaseUserCreate implements Serializable {

    /**
     * 应用id
     */
    @NotNull
    @ApiModelProperty(value = "应用id", required = true)
    @AppValidator
    private Long appId;

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
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;
}
