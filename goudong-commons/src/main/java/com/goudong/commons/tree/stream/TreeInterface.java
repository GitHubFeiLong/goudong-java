package com.goudong.commons.tree.stream;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 接口描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/4/14 9:10
 */
public interface TreeInterface<T extends TreeInterface> {

    Object getId();

    Object getParentId();

    List<T> getChildren();

    void setChildren(List<T> children);

    default List<T> toTree(@NotNull List<T> nodes) {
        return nodes.stream()
                // 获取父节点
                .filter(f->f.getParentId() == null)
                .map(m->{
                    m.setChildren(getChildren(m, nodes));
                    return m;
                }).collect(Collectors.toList());
    }

    default List<T> getChildren(T root, List<T> nodes){
        return nodes.stream().filter(f -> Objects.equals(root.getId(), f.getParentId()))
                .map(m -> {
                    m.setChildren(getChildren(m, nodes));
                    return m;
                }).collect(Collectors.toList());
    }
}
