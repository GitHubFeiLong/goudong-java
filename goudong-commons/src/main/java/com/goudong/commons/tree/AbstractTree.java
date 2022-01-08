package com.goudong.commons.tree;

import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.core.StringUtil;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;

/**
 * 类描述：
 * 树形节点抽象类，统一定义必须要的参数，和构造方法参数校验。定义树形处理时的公用方法
 * @author msi
 * @version 1.0
 * @date 2021/9/30 20:35
 */
@Slf4j
@Getter
public abstract class AbstractTree<T> implements Tree<T> {

    /**
     * 自己元素标识属性名（例如：id）
     */
    protected String selfFieldName;

    /**
     * 父元素标识属性名（例如：parentId）
     */
    protected String parentFieldName;

    /**
     * 装子元素集合的属性名（例如：children）
     */
    protected String childrenFieldName;

    /**
     * 所有节点集合，可能是一维节点集合，也可能是树形结构的集合。
     * 当声明{@link TreeStructureHandler}对象时，那么allNodes就是一维节点集合。
     * 当声明{@link GeneralStructureHandler}对象时，那么allNodes就是树形结构的集合。
     * 注意：因为在转换时，会操作该属性，所以这个属性会改变。
     */
    protected List<T> allNodes;

    /**
     * 树形结构的集合
     */
    protected List<T> treeNodes = new ArrayList<>();

    /**
     * 一维结构的集合
     */
    protected List<T> generalNodes = new ArrayList<>();

    /**
     * selfFieldName，parentFieldName，childrenFieldName 三个属性使用程序默认值，使其可以简写一小部分代码。<br>
     * <ul>
     *     <li>1. selfFieldName=id</li>
     *     <li>1. parentFieldName=parentId</li>
     *     <li>1. childrenFieldName=children</li>
     * </ul>
     *
     * @param allNodes 节点集合，既可以是树形结构的集合，也可以是一般结构的集合
     */
    public AbstractTree(@NotNull @NotEmpty List<T> allNodes) {
        this("id", "parentId", "children", allNodes);
    }

    /**
     * 构造方法，参数进行严格限制
     * 并判断其字段名字（selfFieldName、parentFieldName、childrenFieldName）有效性
     * @param selfFieldName 自己元素标识属性名（例如：id）
     * @param parentFieldName 父元素标识属性名（例如：parentId）
     * @param childrenFieldName 装子元素集合的属性名（例如：children）
     * @param allNodes 所有节点集合，可能是一维节点集合，也可能是树形结构的集合。
     */
    public AbstractTree(@NotBlank String selfFieldName, @NotBlank String parentFieldName, @NotBlank String childrenFieldName, @NotNull @NotEmpty List<T> allNodes) {
        // 先进行校验参数合法性
        try {
            allNodes.get(0).getClass().getDeclaredField(selfFieldName);
            allNodes.get(0).getClass().getDeclaredField(parentFieldName);
            allNodes.get(0).getClass().getDeclaredField(childrenFieldName);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("创建对象失败，原因是参数不正确" + e.getMessage());
        }
        this.selfFieldName = selfFieldName;
        this.parentFieldName = parentFieldName;
        this.childrenFieldName = childrenFieldName;
        this.allNodes = allNodes;
    }

    /**
     * 将树形结构的集合扁平化<br>
     * @param flatNodes 最终扁平化结构集合
     * @param treeNodes 需要处理的树形结构集合
     */
    @SneakyThrows
    protected void flatTreeNodesRecursion (List<T> flatNodes, List<T> treeNodes){
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
     * 递归找到符合条件的node对象
     * @param selfValue node的唯一标识的值
     * @param children 指定集合
     * @return  返回找到的node，并且填充node的子元素属性
     */
    @SneakyThrows
    protected T findBySelfValue2T (Object selfValue, List<T> children) {
        if (CollectionUtils.isEmpty(children)) {
            return null;
        }
        Iterator<T> iterator = children.iterator();
        while (iterator.hasNext()) {
            T node = iterator.next();
            Field declaredField = node.getClass().getDeclaredField(this.selfFieldName);
            declaredField.setAccessible(true);
            // 为防止类型不匹配导致equals不相等，将其都转成string。
            String nodeSelfValue = Optional.ofNullable(declaredField.get(node)).orElse("").toString();
            if (Objects.equals(String.valueOf(selfValue), nodeSelfValue)) {
                // 匹配本次循环，直接返回
                return node;
            }
            // node对象不是我们想要的对象，那么就进行递归node的子元素集合进行查找
            Field childrenDeclaredField = node.getClass().getDeclaredField(this.childrenFieldName);
            childrenDeclaredField.setAccessible(true);
            List<T> nodeChildren = Optional.ofNullable((List<T>)childrenDeclaredField.get(node)).orElse(new ArrayList<T>());

            T childNode = findBySelfValue2T(selfValue, nodeChildren);
            if (childNode != null) {
                return childNode;
            }
            // 引用最初的迭代器，防止循环完第一个根后直接返回null。
            if (!iterator.hasNext()) {
                return null;
            }
        }

        log.error("没有找到您要查找的信息，查找条件：{}", selfValue);
        String clientMessage = StringUtil.format("没有找到您要查找的信息，查找条件：{}", selfValue);
        throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, clientMessage);
    }

}
