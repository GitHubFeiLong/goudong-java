package com.goudong.commons.po;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * authority_user
 * @author
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ApiModel(value="generate.AuthorityUserPO用户基本信息表")
@Data
public class AuthorityUserPO implements Serializable {
    private static final long serialVersionUID = -6147408154544596138L;
    /**
     * uuid
     */
    @ApiModelProperty(value="uuid")
    private String uuid;

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    private String username;

    /**
     * 密码
     */
    @ApiModelProperty(value="密码")
    private String password;

    /**
     * 邮箱
     */
    @ApiModelProperty(value="邮箱")
    private String email;

    /**
     * 手机号
     */
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