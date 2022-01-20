package com.goudong.commons.tree;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.enumerate.core.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.core.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

/**
 * 类描述：
 * 1. 将树形节点转换成一维节点
 * 2. 获取指定节点及其子节点（一维结构）封装 {@link GeneralNode} 对象
 * @author msi
 * @version 1.0
 * @date 2021/9/30 23:27
 */
public class GeneralStructureHandler<T> extends AbstractTree<T> {

    /**
     *  以下属性使用默认值：selfFieldName=id, parentFieldName=parentId, childrenFieldName=children
     * @param allNodes 树形结构node的集合
     */
    public GeneralStructureHandler(List<T> allNodes) {
        super(allNodes);
        // 将参数保留
        super.treeNodes = (List<T>) JSON.parseArray(JSON.toJSONString(allNodes), allNodes.get(0).getClass());
        this.toGeneralStructure();
    }

    /**
     * 构造方法，参数进行严格限制
     * 并判断其字段名字（selfFieldName、parentFieldName、childrenFieldName）有效性
     *
     * @param selfFieldName 自己元素标识属性名（例如：id）
     * @param parentFieldName 父元素标识属性名（例如：parentId）
     * @param childrenFieldName 装子元素集合的属性名（例如：children）
     * @param allNodes 树形结构node的集合。
     */
    public GeneralStructureHandler(String selfFieldName, String parentFieldName, String childrenFieldName, List<T> allNodes) {
        super(selfFieldName, parentFieldName, childrenFieldName, allNodes);
        // 将参数保留
        super.treeNodes = (List<T>) JSON.parseArray(JSON.toJSONString(allNodes), allNodes.get(0).getClass());
        this.toGeneralStructure();
    }

    /**
     * 将一维node集合转换成树形结构集合
     *
     * @return
     */
    @Override
    public List<T> toTreeStructure() {
        return super.treeNodes;
    }

    /**
     * 获取一维结构节点集合
     *
     * @return 返回一维结构节点集合
     */
    @Override
    public List<T> toGeneralStructure() {
        // 如果已经执行过一次了，直接返回结果。
        if (CollectionUtils.isNotEmpty(super.generalNodes)) {
            return super.generalNodes;
        }
        // 递归处理
        toGeneralStructureProcessChildren(super.allNodes);
        return super.generalNodes;
    }

    /**
     * 将 treeNodes 及其子元素递归放入 generalNodes中
     * @param treeNodes 子元素
     */
    @SneakyThrows
    private void toGeneralStructureProcessChildren (List<T> treeNodes) {
        // 结束递归条件
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }

        // 循环处理
        Iterator<T> iterator = treeNodes.iterator();
        while(iterator.hasNext()){
            T parentNode = iterator.next();
            // 将节点加入到集合中去
            super.generalNodes.add(parentNode);
            // 判断是否还有子节点，然后再递归插入集合
            Field childrenDeclaredField = parentNode.getClass().getDeclaredField(super.childrenFieldName);
            childrenDeclaredField.setAccessible(true);
            List<T> children = Optional.ofNullable((List<T>)childrenDeclaredField.get(parentNode)).orElse(new ArrayList<T>());

            // 子元素不为空就递归执行
            if (CollectionUtils.isNotEmpty(children)) {
                toGeneralStructureProcessChildren(children);
            }
        }
    }

    /**
     * 获取指定node的详细信息 一维类型
     *
     * @param selfValue 指定node的唯一标识的值（selfFieldName属性的值）
     * @return 返回 {@link GeneralNode},指定node的信息及其子元素信息（子属性是一维结构）
     */
    @SneakyThrows
    @Override
    public GeneralNode<T> getNodeDetailBySelfValue2GeneralNode(Object selfValue) {
        GeneralNode<T> generalNode = new GeneralNode<>();

        // 因为构造方法传递节点集合是树形结构的，所以获取指定的节点，就能获取它下面的所有子节点
        T selfNode = Optional.ofNullable(super.findBySelfValue2T(selfValue, super.treeNodes))
                .orElseThrow(()->ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, StringUtil.format("未找到指定的节点{}", selfValue)));
        generalNode.setNode(selfNode);

        // 获取该节点的所有子节点
        Field declaredField = selfNode.getClass().getDeclaredField(super.childrenFieldName);
        declaredField.setAccessible(true);
        List<T> children = Optional.ofNullable((List<T>)declaredField.get(selfNode)).orElse(new ArrayList<>());

        // 将children 扁平化
        super.flatTreeNodesRecursion(generalNode.getChildren(), children);

        return generalNode;
    }

    /**
     * 获取指定node的详细信息 树形类型
     *
     * @param selfValue 指定node的唯一标识的值（selfFieldName属性的值）
     * @return 返回该节点，并填充了它子属性（子属性是树形结构）
     */
    @Override
    public T getNodeDetailBySelfValue2T(Object selfValue) {
        return super.findBySelfValue2T(selfValue, super.treeNodes);
    }

}
