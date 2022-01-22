package com.goudong.oauth2.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.goudong.oauth2.po.BaseRolePO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 自定义认证成功对象
 * @author msi
 * @version 1.0
 * @date 2022/1/15 21:54
 */
@Getter
@Setter
@Deprecated
public class AuthenticationImpl implements Authentication {
    //~fields
    //==================================================================================================================

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    @JsonIgnore
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
     * qq登录后，系统获取腾讯的open_id
     */
    private String qqOpenId;

    /**
     * 角色
     */
    private List<BaseRolePO> roles = new ArrayList<>();

    /**
     * 是否已经认证
     */
    private Boolean authenticated = true;

    //~methods
    //==================================================================================================================

    /**
     * 获取权限
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    /**
     * 密码
     * @return
     */
    @Override
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
    public Object getPrincipal() {
        return username;
    }

    /**
     * 是否已经认证
     * @return
     */
    @Override
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
     * 判断账户是否未过期
     * @return
     */
    public boolean isAccountNonExpired() {
        return validTime.after(new Date());
    }
}