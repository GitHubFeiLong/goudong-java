package com.goudong.core.util.tree.v2;

import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 树对象实现该接口，重写方法。即可自动转树
 * @Author e-Feiong.Chen
 * @Date 2022/4/14 9:10
 */
public interface TreeInterface<ID, PID, T> {

    ID getId();

    PID getParentId();

    List<T> getChildren();

    void setChildren(List<T> children);

    /**
     * 转成Tree
     * @param nodes 所有树
     * @return
     */
    static List toTreeByInterface(List<? extends TreeInterface> nodes) {
        AssertUtil.isNotEmpty(nodes);
        return nodes.stream()
                // 获取父节点
                .filter(f->f.getParentId() == null)
                .map(m->{
                    m.setChildren(getChildrenByInterface(m, nodes));
                    return m;
                }).collect(Collectors.toList());
    }

    /**
     * 获取指定节点的子节点
     * @param root
     * @param nodes
     * @return
     */
    static List<TreeInterface> getChildrenByInterface(TreeInterface root, List<? extends TreeInterface> nodes){
        List<TreeInterface> children = nodes.stream().filter(f -> Objects.equals(root.getId(), f.getParentId()))
                .map(m -> {
                    m.setChildren(getChildrenByInterface(m, nodes));
                    return m;
                }).collect(Collectors.toList());
        return children.isEmpty() ? null : children;
    }

    /**
     * 转成普通集合
     * @param nodes
     * @return
     */
    static List<TreeInterface> toFlatByInterface(List<TreeInterface> nodes) {
        AssertUtil.isNotEmpty(nodes);
        List<TreeInterface> flatList = new ArrayList<>();
        nodes.stream().forEach(p->{
            flatList.add(p);
            if (CollectionUtil.isNotEmpty(p.getChildren())) {
                addFlatList(p, flatList);
            }
        });
        return flatList;
    }

    /**
     * 获取指定节点的子节点
     * @param root
     * @param nodes
     * @return
     */
    static void addFlatList(TreeInterface root, List<TreeInterface> nodes){
        ((List<TreeInterface>)root.getChildren()).stream().forEach(p->{
            nodes.add(p);
            if (CollectionUtil.isNotEmpty(p.getChildren())) {
                addFlatList(p, nodes);
            }
            p.setChildren(null);
        });
        root.setChildren(null);
    }
}
