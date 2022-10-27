package com.goudong.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 类描述：
 * 创建用户
 * @author msi
 * @date 2022/1/8 10:26
 * @version 1.0
 */
@Data
@ApiModel
public class BaseUser2CreateDTO implements Serializable {

    private static final long serialVersionUID = -6516564683484609510L;
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value="用户名")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 8, max = 20, message = "password的长度为8~20位")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value="邮箱")
    private String email;

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty(value="手机号")
    private String phone;

    @NotNull(message = "accountRadio不能为null")
    @ApiModelProperty(value="账号单选框值：空字符串、MY_SELF、NOT_MY_SELF")
    private String accountRadio;
}
