package com.goudong.authentication.common.core;

import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * 类描述：
 * 用户token
 * @author cfl
 * @version 1.0
 * @date 2023/7/20 10:42
 */
public class UserToken implements Serializable {

    private static final long serialVersionUID = 9149779931324786687L;

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
    private Collection<String> roles;

    public UserToken() {
    }

    public UserToken(Long id, Long appId, Long realAppId, String username, Collection<String> roles) {
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

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserToken userToken = (UserToken) o;
        return Objects.equals(id, userToken.id) && Objects.equals(appId, userToken.appId) && Objects.equals(realAppId, userToken.realAppId) && Objects.equals(username, userToken.username) && Objects.equals(roles, userToken.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appId, realAppId, username, roles);
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "id=" + id +
                ", appId=" + appId +
                ", realAppId=" + realAppId +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                '}';
    }
}
