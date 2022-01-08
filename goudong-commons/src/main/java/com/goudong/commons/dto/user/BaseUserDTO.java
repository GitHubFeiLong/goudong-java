package com.goudong.commons.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.goudong.commons.frame.jpa.BasePO;
import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.enumerate.user.AccountRadioEnum;
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
     * qq登录后，系统获取腾讯的open_id
     */
    private String qqOpenId;

    /**
     * 账号单选框值：空字符串、MY_SELF、NOT_MY_SELF
     * @see AccountRadioEnum
     */
    private String accountRadio;

    /**
     * 角色
     */
    private List<AuthorityRoleDTO> authorityRoleDTOS;

    /**
     * 菜单
     */
    private List<AuthorityMenuDTO> authorityMenuDTOS;

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
}
