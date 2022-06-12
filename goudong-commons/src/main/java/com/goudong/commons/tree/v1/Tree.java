package com.goudong.commons.tree.v1;

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
     * @return 结果是树形结构集合
     */
    List<T> toTreeStructure();

    /**
     * 将树形结构集合转成一维结构集合
     * @return 结果是一维结构集合
     */
    List<T> toGeneralStructure();

    /**
     * 获取指定node的详细信息 一维类型
     * @param selfValue 指定node的唯一标识的值（selfFieldName属性的值）
     * @return 返回 {@link GeneralNode},指定node的信息及其子元素信息（子属性是一维结构）
     */
    GeneralNode<T> getNodeDetailBySelfValue2GeneralNode(Object selfValue);

    /**
     * 获取指定node的详细信息 树形类型
     * @param selfValue 指定node的唯一标识的值（selfFieldName属性的值）
     * @return 返回该节点，并填充了它子属性（子属性是树形结构）
     */
    T getNodeDetailBySelfValue2T(Object selfValue);
}
