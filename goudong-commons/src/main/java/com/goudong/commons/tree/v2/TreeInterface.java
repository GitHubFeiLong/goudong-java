package com.goudong.commons.tree.v2;

import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotNull;
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
public interface TreeInterface {

    Object getId();

    Object getParentId();

    List getChildren();

    void setChildren(List children);

    /**
     * 转成Tree
     * @param nodes 所有树
     * @return
     */
    static List<TreeInterface> toTreeByInterface(@NotNull List<TreeInterface> nodes) {
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
    static List<TreeInterface> getChildrenByInterface(TreeInterface root, List<TreeInterface> nodes){
        return nodes.stream().filter(f -> Objects.equals(root.getId(), f.getParentId()))
                .map(m -> {
                    m.setChildren(getChildrenByInterface(m, nodes));
                    return m;
                }).collect(Collectors.toList());
    }

    /**
     * 转成普通集合
     * @param nodes
     * @return
     */
    static List<TreeInterface> toFlatByInterface(@NotNull List<TreeInterface> nodes) {
        List<TreeInterface> flatList = new ArrayList<>();
        nodes.stream().forEach(p->{
            flatList.add(p);
            if (CollectionUtils.isNotEmpty(p.getChildren())) {
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
            if (CollectionUtils.isNotEmpty(p.getChildren())) {
                addFlatList(p, nodes);
            }
            p.setChildren(null);
        });
        root.setChildren(null);
    }
}
