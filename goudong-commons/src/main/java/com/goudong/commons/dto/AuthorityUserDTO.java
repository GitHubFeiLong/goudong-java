package com.goudong.commons.dto;

import com.goudong.commons.entity.AuthorityRoleDO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * authority_user
 * @author
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityUserDTO implements Serializable {
    private static final long serialVersionUID = -6147408154544596138L;
    /**
     * uuid
     */
    private String uuid;

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
    private Date validTime;

    /**
     * 是否被删除
     */
    private Boolean isDelete;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * qq登录后，系统获取腾讯的open_id
     */
    private String qqOpenId;

    /**
     * 账号单选框值：空字符串、MY_SELF、NOT_MY_SELF
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
}
