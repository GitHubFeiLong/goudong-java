package com.goudong.commons.tree;

import java.util.List;

/**
 * 接口描述：
 * tree 树形相关的顶层接口
 * @author msi
 * @version 1.0
 * @date 2021/9/30 22:57
 */
public interface Tree<T> {
    /**
     * 将一维node集合转换成树形结构集合
     * @return
     */
    List<T> toTreeStructure();

    /**
     * 将树形结构集合转成一维结构集合
     * @return
     */
    List<T> toGeneralStructure();

    /**
     * 获取指定node的详细信息，包含其所有子node
     * @param selfValue 自己的标识值，例如id
     * @return 根据实现类实现逻辑分为：1. 树形结构的详细信息；2.一维结构的详细信息
     */
    Object getNodeDetailBySelfValue(Object selfValue);
}
