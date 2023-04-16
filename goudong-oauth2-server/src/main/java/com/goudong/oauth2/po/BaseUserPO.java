package com.goudong.oauth2.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;
import com.goudong.commons.constant.user.RoleConst;
import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.framework.jpa.BasePO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.*;

/**
 * 类描述：
 * 用户表
 * @author msi
 * @date 2021/12/19 14:20
 * @version 1.0
 */
@Getter
@Setter
@ToString
@Entity
@Table(name = "base_user")
@SQLDelete(sql = "update base_user set deleted=true where id=?")
@Where(clause = "deleted=false")
@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseUserPO extends BasePO implements UserDetails, Authentication {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = -1209701285445397589L;
    /**
     * 用户名
     */
    @NotBlank(message = "username不能为空")
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 手机号
     */
    @Length(min = 6, max = 32)
    @Column(name = "phone", nullable = false, length = 32)
    private String phone;

    /**
     * 性别（0：未知；1：男；2：女）
     */
    @Column(name = "sex", nullable = false)
    private Integer sex;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 有效截止时间
     */
    @Column(name = "valid_time")
    private Date validTime;

    /**
     * qq登录后，系统获取腾讯的open_id
     */
    @Column(name = "qq_open_id")
    private String qqOpenId;

    /**
     * 是否已经认证
     */
    @Transient
    private Boolean authenticated = true;

    /**
     * 头像地址
     */
    @Column(name = "avatar", nullable = false)
    private String avatar;

    /**
     * 激活状态（true：激活；false：未激活）
     */
    @Column(name = "enabled")
    private Boolean enabled = true;

    /**
     * 锁定状态（true：已锁定；false：未锁定）
     */
    @Column(name = "locked", nullable = false)
    private Boolean locked = false;

    @ManyToMany(targetEntity= BaseRolePO.class, fetch = FetchType.EAGER)
    @JoinTable(name = "base_user_role", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns={@JoinColumn(name = "role_id")})
    private List<BaseRolePO> roles = new ArrayList<>();

    //~methods
    //==================================================================================================================
    /**
     * 创建匿名用户
     * @return
     */
    public static BaseUserPO createAnonymousUser() {
        BaseUserPO anonymousUser = new BaseUserPO();
        anonymousUser.setAuthenticated(true);
        // 这里id不能修改，其他地方已经定义了0是匿名用户
        anonymousUser.setId(0L);
        anonymousUser.setUsername("匿名用户");
        // 创建匿名角色
        BaseRolePO baseRolePO = new BaseRolePO();
        baseRolePO.setId(0L);
        baseRolePO.setRoleName(RoleConst.ROLE_ANONYMOUS);
        baseRolePO.setRoleNameCn("匿名角色");
        anonymousUser.setRoles(Lists.newArrayList(baseRolePO));
        return anonymousUser;
    }

    /**
     * 创建匿名用户
     * @param sessionId 用户会话唯一id
     * @return
     */
    public static BaseUserPO createAnonymousUser(String sessionId) {
        BaseUserPO anonymousUser = new BaseUserPO();
        anonymousUser.setAuthenticated(true);
        // 这里id不能修改，其他地方已经定义了0是匿名用户
        anonymousUser.setId(0L);
        anonymousUser.setUsername("匿名用户");
        anonymousUser.setSessionId(sessionId);
        // 创建匿名角色
        BaseRolePO baseRolePO = new BaseRolePO();
        baseRolePO.setId(0L);
        baseRolePO.setRoleName(RoleConst.ROLE_ANONYMOUS);
        baseRolePO.setRoleNameCn("匿名角色");
        anonymousUser.setRoles(Lists.newArrayList(baseRolePO));
        return anonymousUser;
    }

    /**
     * 获取用户权限，本质上是用户的角色信息
     * @return
     */
    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles;
    }

    /**
     * 判断账户是否未过期
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return validTime.after(new Date());
    }

    /**
     * 判断账户是否未锁定
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return !this.locked;
    }

    /**
     * 判断密码是否未过期
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 判断账户是否可用
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * 密码
     * @return
     */
    @Override
    @Transient
    public Object getCredentials() {
        return password;
    }

    /**
     * 详细信息
     * @return
     */
    @Override
    public Object getDetails() {
        return null;
    }

    /**
     * 用户名
     * @return
     */
    @Override
    @Transient
    public Object getPrincipal() {
        return username;
    }

    /**
     * 是否已经认证
     * @return
     */
    @Override
    @JsonIgnore
    public boolean isAuthenticated() {
        return authenticated;
    }

    /**
     * 设置认证状态
     * @param b
     * @throws IllegalArgumentException
     */
    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = b;
    }

    /**
     * Returns the name of this principal.
     *
     * @return the name of this principal.
     */
    @Override
    public String getName() {
        return this.username;
    }

    /**
     * 自定义的一个会话id，用于区分发起请求的用户，可以是认证过后的token可以是未登录的cookie
     */
    @Transient
    private String sessionId;

    /**
     * 用户对应的菜单
     */
    @Transient
    private Set<BaseMenuDTO> menus;
}
