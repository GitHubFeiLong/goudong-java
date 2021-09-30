package com.goudong.commons.tree;

import java.util.List;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/9/30 23:27
 */
public class GeneralStructureHandler<T> extends BaseTree<T>{
    /**
     * 构造方法，参数进行严格限制
     * 并判断其字段名字（selfFieldName、parentFieldName、childrenFieldName）有效性
     *
     * @param selfFieldName 自己元素标识属性名（例如：id）
     * @param parentFieldName 父元素标识属性名（例如：parentId）
     * @param childrenFieldName 装子元素集合的属性名（例如：children）
     * @param treeNodes 树形结构node的集合。
     */
    public GeneralStructureHandler(String selfFieldName, String parentFieldName, String childrenFieldName, List<T> treeNodes) {
        super(selfFieldName, parentFieldName, childrenFieldName, treeNodes);
    }

    /**
     * 将树形结构集合转成一维结构集合
     *
     * @return
     */
    @Override
    public List<T> toGeneralStructure() {
        return null;
    }

}
