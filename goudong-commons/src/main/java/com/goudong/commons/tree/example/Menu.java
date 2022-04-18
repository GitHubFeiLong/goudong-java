package com.goudong.commons.tree.example;

import com.goudong.commons.tree.stream.TreeInterface;
import lombok.Data;
import lombok.SneakyThrows;

import java.util.List;

/**
 * 类描述：
 * 菜单作为示例
 * @author msi
 * @version 1.0
 * @date 2021/10/1 15:59
 */
@Data
public class Menu implements TreeInterface<Menu> {
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

    // @Override
    // public void setChildren(List children) {
    //     this.setChildren(children);
    // }
}
