package com.goudong.authentication.common.core;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 *
 * @Author Administrator
 * @Version 1.0
 */
public class UserSimple {
    //~fields
    //==================================================================================================================
    /**
     * 用户id
     */
    private Long id;

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 真实应用id（例如xx应用管理员，app_id是认证服务应用的app_id，但是real_app_id是自己所管理xx应用的app_id）
     */
    private Long realAppId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色
     */
    private List<String> roles;

    //~methods
    //==================================================================================================================
    public UserSimple() {
    }

    public UserSimple(Long id, Long appId, Long realAppId, String username, List<String> roles) {
        this.id = id;
        this.appId = appId;
        this.realAppId = realAppId;
        this.username = username;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public Long getRealAppId() {
        return realAppId;
    }

    public void setRealAppId(Long realAppId) {
        this.realAppId = realAppId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSimple that = (UserSimple) o;
        return Objects.equals(id, that.id) && Objects.equals(appId, that.appId) && Objects.equals(realAppId, that.realAppId) && Objects.equals(username, that.username) && Objects.equals(roles, that.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appId, realAppId, username, roles);
    }

    @Override
    public String toString() {
        return "UserSimple{" +
                "id=" + id +
                ", appId=" + appId +
                ", realAppId=" + realAppId +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
