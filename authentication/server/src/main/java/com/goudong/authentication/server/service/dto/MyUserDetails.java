package com.goudong.authentication.server.service.dto;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/18 9:01
 */
@Data
public class MyUserDetails implements UserDetails {

    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户所在appId
     */
    private Long appId;

    /**
     * 登录所选appId,不选择，使用用户所在appId
     */
    private Long selectAppId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 是否激活
     */
    private String password;

    /**
     * 用户id
     */
    private Boolean enabled;

    /**
     * 是否锁定
     */
    private Boolean locked;

    /**
     * 有效时长
     */
    private Date validTime;

    /**
     * 角色
     */
    private List<GrantedAuthority> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return validTime == null || (validTime.after(new Date()));
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
