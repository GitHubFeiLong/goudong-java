package com.goudong.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * 修改密码
 * @author cfl
 * @version 1.0
 * @date 2023/7/2 9:01
 */
@Data
public class ChangeOwnPasswordReq {
    //~fields
    //==================================================================================================================
    @NotBlank
    @ApiModelProperty(value = "密码", required = true)
    private String password;
    //~methods
    //==================================================================================================================
}
