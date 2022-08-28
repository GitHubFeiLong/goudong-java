package com.goudong.commons.dto.user;

import com.goudong.commons.annotation.validator.PhoneValidator;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 创建简单的用户
 * @author cfl
 * @date 2022/8/28 14:38
 * @version 1.0
 */
@Data
@ApiModel
public class SimpleCreateUserReq implements Serializable {

    private static final long serialVersionUID = -6516564683484609510L;
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty(value="用户名", required = true)
    private String username;

    @NotBlank(message = "邮箱不能为空")
    @ApiModelProperty(value="邮箱", required = true)
    private String email;

    @PhoneValidator(message = "phone格式错误")
    @ApiModelProperty(value="手机号", required = true)
    private String phone;

    @NotEmpty(message = "角色不能为空")
    @ApiModelProperty(value = "用户绑定的角色", required = true)
    private List<Long> roleIds;
}
