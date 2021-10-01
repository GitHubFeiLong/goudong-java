package com.goudong.commons.tree;

import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.StringUtil;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.*;

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
     * 一维节点集合
     */
    private List<T> generalNodes = new ArrayList<>();


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
    public List<T> toGeneralStructure() throws NoSuchFieldException, IllegalAccessException {
        // 如果已经执行过一次了，直接返回结果。
        if (CollectionUtils.isNotEmpty(this.generalNodes)) {
            return this.generalNodes;
        }
        // 递归处理
        toGeneralStructureProcessChildren(this.allNodes);
        return this.generalNodes;
    }

    /**
     * 将 treeNodes 及其子元素递归放入 generalNodes中
     * @param treeNodes 子元素
     */
    private void toGeneralStructureProcessChildren (List<T> treeNodes) throws NoSuchFieldException, IllegalAccessException {
        // 结束递归条件
        if (CollectionUtils.isEmpty(treeNodes)) {
            return;
        }

        // 循环处理
        Iterator<T> iterator = treeNodes.iterator();
        while(iterator.hasNext()){
            T parentNode = iterator.next();
            // 将节点加入到集合中去
            this.generalNodes.add(parentNode);
            // 判断是否还有子节点，然后再递归插入集合
            Field childrenDeclaredField = parentNode.getClass().getDeclaredField(this.childrenFieldName);
            childrenDeclaredField.setAccessible(true);
            List<T> children = Optional.ofNullable((List<T>)childrenDeclaredField.get(parentNode)).orElse(new ArrayList<T>());

            // 子元素不为空就递归执行
            if (CollectionUtils.isNotEmpty(children)) {
                toGeneralStructureProcessChildren(children);
            }
        }
    }

    /**
     * 获取指定node的详细信息，包含其所有子node
     *
     * @param selfValue 自己的标识值，例如id
     * @return 根据实现类实现逻辑分为：1. 树形结构的详细信息；2.一维结构的详细信息
     */
    @Override
    public GeneralNode<T> getNodeDetailBySelfValue(Object selfValue) throws NoSuchFieldException, IllegalAccessException {

        GeneralNode<T> generalNode = new GeneralNode<>();

        // 因为构造方法传递节点集合是树形结构的，所以获取指定的节点，就能获取它下面的所有子节点
        T selfNode = Optional.ofNullable(this.findBySelfValue(selfValue, this.allNodes))
                .orElseThrow(()->ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, StringUtil.format("未找到指定的节点{}", selfValue)));
        generalNode.setNode(selfNode);

        // 获取该节点的所有子节点
        Field declaredField = selfNode.getClass().getDeclaredField(this.childrenFieldName);
        declaredField.setAccessible(true);
        List<T> children = Optional.ofNullable((List<T>)declaredField.get(selfNode)).orElse(new ArrayList<>());

        // 将children 扁平化
        this.flatTreeNodesRecursion(generalNode.getChildren(), children);

        return generalNode;
    }

    /**
     * 根据selfValue进行查找树形结构集合中的节点
     * @param selfValue 条件
     * @param children 集合
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private T findBySelfValue(Object selfValue, List<T> children) throws NoSuchFieldException, IllegalAccessException {
        if (CollectionUtils.isEmpty(children)) {
            return null;
        }
        Iterator<T> iterator = children.iterator();
        while(iterator.hasNext()) {
            T node = iterator.next();
            Field selfDeclaredField = node.getClass().getDeclaredField(this.selfFieldName);
            selfDeclaredField.setAccessible(true);
            Object nodeSelfValue = selfDeclaredField.get(node);
            // 找打了指定节点，就直接返回不在继续执行
            if (Objects.equals(selfValue, nodeSelfValue)) {
                return node;
            } else { // 没找到，就继续从子节点中找
                // 准备子元素进行递归
                Field childrenDeclaredField = node.getClass().getDeclaredField(this.childrenFieldName);
                childrenDeclaredField.setAccessible(true);
                List<T> nodeChildren = Optional.ofNullable((List<T>)childrenDeclaredField.get(node)).orElse(new ArrayList<T>());
                // 递归查找子元素是否符合条件
                T bySelfValue = findBySelfValue(selfValue, nodeChildren);
                // 存在符合条件的，就直接返回
                if (bySelfValue != null) {
                    return bySelfValue;
                }
            }
        }
        // 没有符合条件的，直接返回null。
        return null;
    }

    /**
     * 将树形结构的集合扁平化
     * @param flatNodes 最终扁平化结构集合
     * @param treeNodes 需要处理的树形结构集合
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private void flatTreeNodesRecursion (List<T> flatNodes, List<T> treeNodes) throws NoSuchFieldException, IllegalAccessException {
        if(CollectionUtils.isEmpty(treeNodes)) {
            return;
        }
        Iterator<T> iterator = treeNodes.iterator();
        while(iterator.hasNext()) {
            T node = iterator.next();
            // 添加结果到集合
            flatNodes.add(node);

            // 节点的子节点，递归进行执行添加
            Field childrenDeclaredField = node.getClass().getDeclaredField(this.childrenFieldName);
            childrenDeclaredField.setAccessible(true);
            List<T> children = Optional.ofNullable((List<T>)childrenDeclaredField.get(node)).orElse(new ArrayList());
            flatTreeNodesRecursion(flatNodes, children);
        }
    }

    /**
     * 将一维node集合转换成树形结构集合
     *
     * @return
     */
    @Override
    public List<T> toTreeStructure() {
        return this.allNodes;
    }

    /**
     * 获取一维结构的集合
     * @return
     */
    public List<T> getGeneralNodes() throws NoSuchFieldException, IllegalAccessException {
        return this.toGeneralStructure();
    }

    /**
     * 节点及其子节点集合
     * @param <T>
     */
    @Data
    public static class GeneralNode<T>{
        /**
         * 查找的本身对象
         */
        private T node;

        /**
         * 查找node的子元素集合(一维结构)
         */
        private List<T> children = new ArrayList<>();
    }
}
