package com.goudong.commons.tree;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 在获取指定节点的详细信息时的对象
 * @author msi
 * @version 1.0
 * @date 2021/10/23 15:42
 */
@Data
public class GeneralNode<T> {
    /**
     * 查找的本身对象
     */
    private T node;

    /**
     * 查找node的子元素集合
     */
    private List<T> children = new ArrayList<>();
}
