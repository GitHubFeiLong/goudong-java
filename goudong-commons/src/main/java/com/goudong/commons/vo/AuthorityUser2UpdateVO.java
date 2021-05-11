package com.goudong.commons.vo;

import com.goudong.commons.validated.Create;
import com.goudong.commons.validated.Update;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * 创建用户
 * @Author msi
 * @Date 2021-05-11 17:51
 * @Version 1.0
 */
@ApiModel
@Data
public class AuthorityUser2UpdateVO implements Serializable {
    private static final long serialVersionUID = 2835077371402785965L;
    /**
     * uuid
     */
    @ApiModelProperty(value="uuid")
    private String uuid;

    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为空", groups = {Create.class, Update.class})
    @ApiModelProperty(value="用户名")
    private String username;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {Create.class, Update.class})
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {Create.class, Update.class})
    @Email(message = "邮箱格式不正确", groups = {Create.class, Update.class})
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空", groups = {Create.class, Update.class})
    @ApiModelProperty(value="手机号")
    private String phone;

    /**
     * 昵称
     */
    @ApiModelProperty(value="昵称")
    private String nickname;

    /**
     * 备注
     */
    @ApiModelProperty(value="备注")
    private String remark;

    /**
     * 有效截止时间
     */
    @ApiModelProperty(value="有效截止时间")
    private Date validTime;

    /**
     * 是否被删除
     */
    @ApiModelProperty(value="是否被删除")
    private Boolean isDelete;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间")
    private Date updateTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间")
    private Date createTime;

    /**
     * qq登录后，系统获取腾讯的open_id
     */
    @ApiModelProperty(value="qq登录后，系统获取腾讯的open_id")
    private String qqOpenId;
}
