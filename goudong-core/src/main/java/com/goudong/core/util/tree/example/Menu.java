package com.goudong.core.util.tree.example;


import com.goudong.core.util.tree.v2.TreeInterface;

import java.util.List;

/**
 * 类描述：
 * 菜单作为示例
 * @author msi
 * @version 1.0
 * @date 2021/10/1 15:59
 */
public class Menu implements TreeInterface {
    /**
     * 主键id
     */
    private Integer id;

    /**
     * 父级id
     */
    private Integer parentId;

    /**
     * 子集
     */
    private List<Menu> children;

    public Menu(Integer id, Integer parentId, List<Menu> children) {
        this.id = id;
        this.parentId = parentId;
        this.children = children;
    }

    @Override
    public void setChildren(List children) {
        this.children = children;
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    @Override
    public List<Menu> getChildren() {
        return children;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "id=" + id +
                ", parentId=" + parentId +
                ", children=" + children +
                '}';
    }
}
