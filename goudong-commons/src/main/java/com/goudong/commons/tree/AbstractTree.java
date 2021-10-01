package com.goudong.commons.tree;

import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 类描述：
 * 树形节点抽象类，统一定义必须要的参数，和构造方法参数校验
 * @author msi
 * @version 1.0
 * @date 2021/9/30 20:35
 */
@Slf4j
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
     * 当声明的对象是处理一维节点集合，那么allNodes就是一维节点集合。
     * 当声明的对象是处理树形结构的集合，那么allNodes就是树形结构的集合。
     */
    protected List<T> allNodes;


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
}
