package com.goudong.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goudong.commons.enumerate.user.AccountRadioEnum;
import com.goudong.commons.framework.jpa.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * authority_user
 * @author
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BaseUserDTO extends BasePO {
    private static final long serialVersionUID = -6147408154544596138L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 备注
     */
    private String remark;

    /**
     * 有效截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime validTime;

    /**
     * 账号单选框值：空字符串、MY_SELF、NOT_MY_SELF
     * @see AccountRadioEnum
     */
    private String accountRadio;

    /**
     * 用户名、电话或邮箱
     */
    private String loginName;

    /**
     * 绑定的openId类型：QQ，WE_CHAT
     */
    private String userType;

    /**
     * 验证码
     */
    private String code;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 激活状态（true：激活；false：未激活）
     */
    private Boolean enabled;

    /**
     * 性别（0：未知；1：男；2：女）
     */
    private Integer sex;
    /**
     * 锁定状态（true：已锁定；false：未锁定）
     */
    private Boolean locked;

    @ApiModelProperty("角色中文名集合")
    private List<String> roleNameCn;

    @ApiModelProperty("角色id集合")
    private List<Long> roleIds;
}
