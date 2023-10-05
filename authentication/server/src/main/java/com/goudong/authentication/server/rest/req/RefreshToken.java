package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * 刷新token请求对象
 * @Author Administrator
 * @Version 1.0
 */
@Data
public class RefreshToken {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "刷新token", required = true)
    @NotBlank
    private String refreshToken;
    //~methods
    //==================================================================================================================
}
