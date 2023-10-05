package com.goudong.authentication.common.core;

import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 登录返回信息
 * @author cfl
 * @version 1.0
 * @date 2023/7/18 13:44
 */
public class LoginResp {

    private Long id;

    private Long appId;

    private Long realAppId;

    private String username;

    private Token token;

    private List<String> roles;

    /**
     * 应用的首页地址
     */
    private String homePage;

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

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public String getHomePage() {
        return homePage;
    }

    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginResp loginResp = (LoginResp) o;
        return Objects.equals(id, loginResp.id) && Objects.equals(appId, loginResp.appId) && Objects.equals(realAppId, loginResp.realAppId) && Objects.equals(username, loginResp.username) && Objects.equals(token, loginResp.token) && Objects.equals(roles, loginResp.roles) && Objects.equals(homePage, loginResp.homePage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, appId, realAppId, username, token, roles, homePage);
    }

    @Override
    public String toString() {
        return "LoginResp{" +
                "id=" + id +
                ", appId=" + appId +
                ", realAppId=" + realAppId +
                ", username='" + username + '\'' +
                ", token=" + token +
                ", roles=" + roles +
                ", homePage='" + homePage + '\'' +
                '}';
    }
}


