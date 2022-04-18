package com.goudong.commons.tree.stream;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Tree<T extends TreeInterface> {

    public List<T> toTree(@NotNull List<T> nodes) {
        return nodes.stream()
                // 获取父节点
                .filter(f->f.getParentId() == null)
                .map(m->{
                    m.setChildren(getChildren(m, nodes));
                    return m;
                }).collect(Collectors.toList());
    }

    public List<T> getChildren(T root, List<T> nodes){
        return nodes.stream().filter(f -> Objects.equals(root.getId(), f.getParentId()))
                .map(m -> {
                    m.setChildren(getChildren(m, nodes));
                    return m;
                }).collect(Collectors.toList());
    }
}
