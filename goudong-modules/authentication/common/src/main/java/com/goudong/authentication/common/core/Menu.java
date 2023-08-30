package com.goudong.authentication.common.core;

import java.util.Objects;

/**
 * 类描述：
 * 菜单
 * @ClassName Menu
 * @Author Administrator
 * @Date 2023/8/28 20:29
 * @Version 1.0
 */
public class Menu {
    //~fields
    //==================================================================================================================

    private Long id;

    private Long parentId;

    private String permissionId;

    private String name;

    private Integer type;

    private String path;

    private String method;

    private Integer sortNum;

    private Boolean hide;

    private String meta;

    private String remark;
    //~methods
    //==================================================================================================================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(String permissionId) {
        this.permissionId = permissionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Integer getSortNum() {
        return sortNum;
    }

    public void setSortNum(Integer sortNum) {
        this.sortNum = sortNum;
    }

    public Boolean getHide() {
        return hide;
    }

    public void setHide(Boolean hide) {
        this.hide = hide;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(parentId, menu.parentId) && Objects.equals(permissionId, menu.permissionId) && Objects.equals(name, menu.name) && Objects.equals(type, menu.type) && Objects.equals(path, menu.path) && Objects.equals(method, menu.method) && Objects.equals(sortNum, menu.sortNum) && Objects.equals(hide, menu.hide) && Objects.equals(meta, menu.meta) && Objects.equals(remark, menu.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, parentId, permissionId, name, type, path, method, sortNum, hide, meta, remark);
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", permissionId='" + permissionId + '\'' +
                ", name='" + name + '\'' +
                ", type=" + type +
                ", path='" + path + '\'' +
                ", method='" + method + '\'' +
                ", sortNum=" + sortNum +
                ", hide=" + hide +
                ", meta='" + meta + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
