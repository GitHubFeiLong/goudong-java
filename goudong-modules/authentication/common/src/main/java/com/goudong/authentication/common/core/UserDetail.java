package com.goudong.authentication.common.core;

import java.util.Collection;
import java.util.List;

/**
 * 类描述：
 *
 * @ClassName UserInfoToken
 * @Author Administrator
 * @Date 2023/8/28 20:25
 * @Version 1.0
 */
public class UserDetail {
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
     * 应用id
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

    /**
     * 菜单
     */
    private List<Menu> menus;
    //~methods
    //==================================================================================================================

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

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<Menu> getMenus() {
        return menus;
    }

    public void setMenus(List<Menu> menus) {
        this.menus = menus;
    }
}
