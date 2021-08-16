package com.goudong.commons.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 创建用户
 * @Author msi
 * @Date 2021-05-11 17:51
 * @Version 1.0
 */
@Data
@ApiModel
public class AuthorityUser2CreateVO implements Serializable {

    private static final long serialVersionUID = -6516564683484609510L;
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value="用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    @ApiModelProperty(value="邮箱")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value="手机号")
    private String phone;

    @NotNull(message = "accountRadio不能为null")
    @ApiModelProperty(value="账号单选框值：空字符串、MY_SELF、NOT_MY_SELF")
    private String accountRadio;
}
