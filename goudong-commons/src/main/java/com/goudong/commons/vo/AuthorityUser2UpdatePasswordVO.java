package com.goudong.commons.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 修改密码
 * @Author msi
 * @Date 2021-05-11 17:51
 * @Version 1.0
 */
@Data
@ApiModel
@Deprecated
public class AuthorityUser2UpdatePasswordVO implements Serializable {

    private static final long serialVersionUID = 7929990337110946181L;

    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(value = "用户id", required = true)
    private Long id;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty(value = "验证码", required = true)
    private String code;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码",required = true)
    private String password;
}
