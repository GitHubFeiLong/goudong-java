package com.goudong.commons.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
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
public class AuthorityUser2UpdatePasswordVO implements Serializable {

    @NotBlank(message = "用户主键uuid不能为空")
    @ApiModelProperty(value = "用户主键")
    private String uuid;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;
}
