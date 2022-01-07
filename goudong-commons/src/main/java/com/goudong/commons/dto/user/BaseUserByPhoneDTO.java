package com.goudong.commons.dto.user;

import com.goudong.commons.core.jpa.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 类描述：
 * 根据手机号获取用户
 * @auther msi
 * @date 2022/1/7 17:41
 * @version 1.0
 */
@Data
@ApiModel(value="根据手机号获取用户信息")
public class BaseUserByPhoneDTO extends BasePO {

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
