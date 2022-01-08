package com.goudong.commons.tree;

import com.alibaba.fastjson.JSON;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.core.StringUtil;
import lombok.SneakyThrows;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 树形结构集合的处理器
 * @author msi
 * @version 1.0
 * @date 2021/9/30 23:27
 */
public class TreeStructureHandler<T> extends AbstractTree<T> {

    /**
     *  以下属性使用默认值：selfFieldName=id, parentFieldName=parentId, childrenFieldName=children
     * @param allNodes 节点集合，一般结构的集合
     */
    public TreeStructureHandler(List<T> allNodes) {
        super(allNodes);
        // 将参数保留
        super.generalNodes = (List<T>) JSON.parseArray(JSON.toJSONString(allNodes), allNodes.get(0).getClass());
        // 直接初始化tree
        this.toTreeStructure();
    }

    /**
     * 构造方法，参数进行严格限制
     * 并判断其字段名字（selfFieldName、parentFieldName、childrenFieldName）有效性
     *
     * @param selfFieldName 自己元素标识属性名（例如：id）
     * @param parentFieldName 父元素标识属性名（例如：parentId）
     * @param childrenFieldName 装子元素集合的属性名（例如：children）
     * @param allNodes 一般结构的集合
     */
    public TreeStructureHandler(String selfFieldName, String parentFieldName, String childrenFieldName, List<T> allNodes) {
        super(selfFieldName, parentFieldName, childrenFieldName, allNodes);
        // 将参数保留
        super.generalNodes = (List<T>) JSON.parseArray(JSON.toJSONString(allNodes), allNodes.get(0).getClass());
        // 直接初始化tree
        this.toTreeStructure();
    }

    /**
     * 将属性allNodes进行一系列的处理，最终使其达到树形结构
     *
     * @return 返回树形结构的集合对象
     */
    @SneakyThrows
    @Override
    public List<T> toTreeStructure() {
        // 已经执行过一次该方法，就直接会结果
        if (CollectionUtils.isNotEmpty(super.treeNodes)) {
            return super.treeNodes;
        }

        List<T> parentNodes = new ArrayList<>();
        // 循环一维节点集合
        Iterator<T> iterator = super.allNodes.iterator();
        while (iterator.hasNext()) {
            T node = iterator.next();
            /*
                通过反射获取对象父属性字段的值。
             */
            Field parentDeclaredField = node.getClass().getDeclaredField(super.parentFieldName);
            parentDeclaredField.setAccessible(true);
            Object parent = parentDeclaredField.get(node);

            // 对象的父元素字段的值是空，表示该对象是根对象，将其添加到集合
            if (parent == null) {
                parentNodes.add(node);
            }
        }

        // 无树形结构，直接返回。
        if (parentNodes.size() == super.allNodes.size()) {
            super.treeNodes = parentNodes;
            return parentNodes;
        }

        /*
            有树形结构，需要将子元素设置到父元素的 childrenField 属性中去
         */
        toTreeStructureProcessChildren(parentNodes);

        super.treeNodes = parentNodes;
        return parentNodes;
    }

    /**
     * 处理父节点的children属性。
     * @param parentNodes 父级节点集合
     */
    @SneakyThrows
    private void toTreeStructureProcessChildren(List<T> parentNodes) {
        // 循环父元素节点们
        Iterator<T> iterator = parentNodes.iterator();
        while (iterator.hasNext()) {
            T parentNode = iterator.next();
            /*
                获取自己的属性的值，找到它的所有子节点。
             */
            Field selfDeclaredField = parentNode.getClass().getDeclaredField(super.selfFieldName);
            selfDeclaredField.setAccessible(true);
            Object self = selfDeclaredField.get(parentNode);

            // 获取子元素
            List<T> children = super.allNodes.stream().filter(f -> {
                try {
                    // 父元素值（parentId）等于parentNode的属性值（id）,直接返回
                    Field parentDeclaredField = f.getClass().getDeclaredField(super.parentFieldName);
                    parentDeclaredField.setAccessible(true);
                    Object parent = parentDeclaredField.get(f);

                    return Objects.equals(self, parent);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                return false;
            }).collect(Collectors.toList());

            // 设置子元素到父节点的 childrenField 中
            Field childrenDeclaredField = parentNode.getClass().getDeclaredField(super.childrenFieldName);
            childrenDeclaredField.setAccessible(true);
            // 将本次获得子元属添加进去
            childrenDeclaredField.set(parentNode, children);

            // 递归设置子元素
            toTreeStructureProcessChildren(children);
        }
    }

    /**
     * 获取一维结构节点集合
     *
     * @return 返回一维结构节点集合
     */
    @Override
    public List<T> toGeneralStructure(){
        return super.getGeneralNodes();
    }

    /**
     * 获取指定node的详细信息 一维类型
     *
     * @param selfValue 指定node的唯一标识的值（selfFieldName属性的值）
     * @return 返回 {@link GeneralNode},指定node的信息及其子元素信息（子属性是一维结构）
     */
    @SneakyThrows
    @Override
    public GeneralNode<T> getNodeDetailBySelfValue2GeneralNode(Object selfValue){
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
    public T getNodeDetailBySelfValue2T(Object selfValue){
        return super.findBySelfValue2T(selfValue, super.treeNodes);
    }

}
