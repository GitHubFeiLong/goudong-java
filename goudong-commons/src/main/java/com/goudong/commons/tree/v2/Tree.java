package com.goudong.commons.tree.v2;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.goudong.commons.function.core.SFunction;
import com.goudong.commons.tree.example.Menu;
import com.goudong.commons.tree.v1.AbstractTree;
import com.goudong.commons.utils.core.ColumnUtil;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 树工具，使用反射或指定类型实现。
 * @author msi
 * @version 1.0
 * @date 2022/6/12 17:28
 */
public class Tree {

    //~fields
    //==================================================================================================================
    private boolean reflectType = false;
    /**
     * 树的唯一标识
     */
    private Field id;
    /**
     * 树父亲的唯一标识
     */
    private Field parentId;
    /**
     * 树的儿子集合
     */
    private Field children;
    /**
     * 设置儿子集合的方法
     */
    private Method setChildren;
    /**
     * 获取儿子集合的方法
     */
    private Method getChildren;

    //~methods
    //==================================================================================================================
    public static void main(String[] args) {
        // 第一棵树
        Menu menu1 = new Menu(1, null, null);
        Menu menu2 = new Menu(2, 1, null);
        Menu menu3 = new Menu(3, 1, null);
        Menu menu4 = new Menu(4, 2, null);
        Menu menu5 = new Menu(5, 4, null);
        ArrayList<Menu> list1 = Lists.newArrayList(menu1, menu2, menu3, menu4, menu5);
        List<Menu> menus = Tree.getInstance().id(Menu::getId).parentId(Menu::getParentId).children(Menu::getChildren).toTree(list1);

        List<Menu> menus1 = Tree.getInstance().toTree(list1);
        List<Menu> children = menus1.get(0).getChildren();
        System.out.println("JSON.toJSONString(menus) = " + JSON.toJSONString(menus1));
        System.out.println("JSON.toJSONString(menus) = " + JSON.toJSONString(menus));

        List<Menu> menus2 = Tree.getInstance().children(Menu::getChildren).toFlat(menus1);
        System.out.println("JSON.toJSONString(menus) = " + JSON.toJSONString(menus2));
    }

    private Tree(){

    }

    public static Tree getInstance() {
        return new Tree();
    }

    /**
     * 设置树的id字段
     * @param id
     * @param <T>
     * @return
     */
    public <T> Tree id(SFunction<T, ?> id) {
        this.id = ColumnUtil.getField(id);
        reflectType = true;
        return this;
    }

    /**
     * 设置树的父id字段
     * @param parentId
     * @param <T>
     * @return
     */
    public <T> Tree parentId(SFunction<T, ?> parentId) {
        this.parentId = ColumnUtil.getField(parentId);
        reflectType = true;
        return this;
    }

    /**
     * 设置树的子节点字段
     * @param children
     * @param <T>
     * @return
     */
    public <T> Tree children(SFunction<T, ?> children) {
        this.children = ColumnUtil.getField(children);
        reflectType = true;
        return this;
    }

    /**
     * 转成树型结构
     * @param nodes
     * @param <E>
     * @return
     */
    public <E> List<E> toTree(@NotNull List<E> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return new ArrayList();
        }

        // 元素实现了 TreeInterface接口
        if (TreeInterface.class.isInstance(nodes.get(0))) {
            return (List<E>) TreeInterface.toTreeByInterface((List<TreeInterface>) nodes);
        }

        // 使用反射的方式
        if (this.reflectType) {
            return toTreeByReflect(nodes);
        }

        throw new RuntimeException("集合转换成树错误，请尝试将节点继承"+ AbstractTree.class.getName() + "或设置成员变量等方法");
    }

    /**
     * 将树扁平成一维
     * @param nodes 树节点
     * @param <E>
     * @return
     */
    public <E> List<E> toFlat(@NotNull List<E> nodes) {
        if (CollectionUtils.isEmpty(nodes)) {
            return new ArrayList();
        }

        // 元素实现了 TreeInterface接口
        if (TreeInterface.class.isInstance(nodes.get(0))) {
            return (List<E>)TreeInterface.toFlatByInterface((List<TreeInterface>)nodes);
        }

        // 使用反射的方式
        return toFlatByReflect(nodes);
    }

    /**
     * 使用反射的方式转成Tree
     * @param nodes
     * @param <E>
     * @return
     */
    private <E> List<E> toTreeByReflect(@NotNull @NotEmpty List<E> nodes) {
        // 检查字段及方法有效
        checkToTreePrecondition();

        // 设置setChildren值
        setChildrenMethod(nodes);

        return nodes.stream()
                // 获取父节点
                .filter(f-> {
                    try {
                        return parentId.get(f) == null;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .map(m->{
                    try {
                        setChildren.invoke(m, getChildrenByReflect(m, nodes));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return m;
                }).collect(Collectors.toList());
    }

    /**
     * 获取指定节点的所有子节点
     * @param root 指定树节点
     * @param nodes 所有节点
     * @return
     */
    private List<?> getChildrenByReflect(Object root, List<?> nodes){
        return nodes.stream().filter(f -> {
                    try {
                        return Objects.equals(id.get(root), parentId.get(f));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return false;
                })
                .map(m -> {
                    try {
                        setChildren.invoke(m, getChildrenByReflect(m, nodes));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    return m;
                }).collect(Collectors.toList());
    }

    /**
     * 转换树时的前置条件校验
     * 1. 成员变量不能为空
     * 2. 有setChildren方法
     */
    private void checkToTreePrecondition() {
        if (id == null) {
            throw new RuntimeException("请设置树的id值");
        }
        if (parentId == null) {
            throw new RuntimeException("请设置树的父id值");
        }
        if (children == null) {
            throw new RuntimeException("请设置树的children值");
        }
        /*
            设置私有属性能访问
         */
        id.setAccessible(true);
        parentId.setAccessible(true);
        children.setAccessible(true);
    }

    /**
     * 转换树时的前置条件校验
     * 1. 成员变量不能为空
     * 2. 有setChildren方法
     */
    private void checkToFlatPrecondition() {
        if (children == null) {
            throw new RuntimeException("请设置树的children值");
        }
        children.setAccessible(true);
    }

    /**
     * 转换成普通集合使用反射方式
     * @param nodes
     * @param <E>
     * @return
     */
    private <E> List<E> toFlatByReflect(List<E> nodes) {
        // 检查字段及方法有效
        checkToFlatPrecondition();
        // 设置setChildren值
        setChildrenMethod(nodes);

        List<E> flatList = new ArrayList<>();
        nodes.stream().forEach(p->{
            flatList.add(p);
            try {
                if (CollectionUtils.isNotEmpty((Collection<?>) getChildren.invoke(p))) {
                    addFlatList(p, flatList);
                }
                setChildren.invoke(p, Lists.newArrayList());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

        return flatList;
    }

    /**
     * 添加元素到容器nodes
     * @param root 指定节点
     * @param nodes 容器
     * @param <T> 集合类型
     */
    private <T> void addFlatList(T root, List<T> nodes) {
        try {
            ((List<T>)getChildren.invoke(root)).stream().forEach(p->{
                nodes.add(p);
                try {
                    if (CollectionUtils.isNotEmpty((Collection<?>) getChildren.invoke(p))) {
                        addFlatList(p, nodes);
                    }
                    setChildren.invoke(p, Lists.newArrayList());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            setChildren.invoke(root, Lists.newArrayList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 设置成员变量 setChildren
     * @param nodes
     */
    private void setChildrenMethod(List nodes) {
        Class<?> nodeClass = nodes.get(0).getClass();
        char[] charArray = this.children.getName().toCharArray();
        if (charArray[0] >= 'a' && charArray[0] <= 'z') {
            charArray[0] -= 32;
        }

        String setChildrenMethodName = "set" + String.valueOf(charArray);
        String getChildrenMethodName = "get" + String.valueOf(charArray);
        try {
            this.setChildren = nodeClass.getDeclaredMethod(setChildrenMethodName, List.class);
            this.getChildren = nodeClass.getDeclaredMethod(getChildrenMethodName);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("children属性没有set方法");
        }
    }
}