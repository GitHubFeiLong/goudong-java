package com.goudong.user.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 类描述：
 * 绑定openId，qq或微信
 * @auther msi
 * @date 2022/1/26 14:31
 * @version 1.0
 */
@Data
@ApiModel
public class BaseUser2UpdateOpenIdDTO implements Serializable {

    private static final long serialVersionUID = 4349104239210189151L;
    /**
     * qq登录后，系统获取腾讯的open_id
     */
    @NotBlank(message = "openId为必填项")
    @ApiModelProperty(value="qq登录后，系统获取腾讯的open_id")
    private String qqOpenId;

    @NotBlank(message = "用户名、电话或邮箱为必填项")
    @ApiModelProperty(value = "用户名、电话或邮箱")
    private String loginName;

    @NotBlank(message = "密码不能为空")
    @ApiModelProperty(value = "密码")
    private String password;

    @NotBlank(message = "userType 不能为空")
    @ApiModelProperty(value = "绑定的openId类型：QQ，WE_CHAT")
    private String userType;
}
