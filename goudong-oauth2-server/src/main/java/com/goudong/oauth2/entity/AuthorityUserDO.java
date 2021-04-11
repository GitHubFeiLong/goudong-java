package com.goudong.oauth2.entity;

import com.zuopei.commons.validated.Create;
import com.zuopei.commons.validated.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @Author msi
 * @Date 2021-04-02 13:10
 * @Version 1.0
 */
@Data
@Accessors(chain = true)
@ApiModel
public class AuthorityUserDO implements Serializable {
    private static final long serialVersionUID = 5738896587320052883L;
    @ApiModelProperty(value = "用户表主键uuid")
    private String uuid;

    @NotBlank(message = "{username.empty}", groups = {Create.class, Update.class})
    @ApiModelProperty(value = "用户名")
    private String username;
    @NotBlank(message = "密码不能为空", groups = {Create.class, Update.class})
    @ApiModelProperty(value = "密码")
    private String password;
    @Email(message = "邮箱不能为空", groups = {Create.class, Update.class})
    @ApiModelProperty(value = "邮箱")
    private String email;
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "昵称")
    private String nickname;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "有效时间")
    private Date validTime;
    @ApiModelProperty(value = "是否被删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "角色")
    private List<AuthorityRoleDO> authorityRoleDOS;
}
