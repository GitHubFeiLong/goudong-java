package com.goudong.commons.vo;

import com.goudong.commons.po.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * authority_user
 * @author
 */
@Data
@ApiModel(value="generate.AuthorityUserVO用户基本信息表")
public class AuthorityUserVO extends BasePO {

    private static final long serialVersionUID = 6520160201127140443L;

    /**
     * 用户名
     */
    @ApiModelProperty(value="用户名")
    private String username;

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
    private LocalDateTime validTime;

    /**
     * qq登录后，系统获取腾讯的open_id
     */
    @ApiModelProperty(value="qq登录后，系统获取腾讯的open_id")
    private String qqOpenId;
}
