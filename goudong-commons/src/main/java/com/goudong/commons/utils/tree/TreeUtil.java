package com.goudong.commons.utils.tree;

import com.google.common.collect.Lists;
import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.StringUtil;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 树形节点工具封装
 * @author msi
 * @version 1.0
 * @date 2021/9/30 20:35
 */
@Getter
@Slf4j
public class TreeUtil<T> {

    /**
     * 自己元素标识属性名（例如：id）
     */
    private String selfFieldName;
    /**
     * 父元素标识属性名（例如：parentId）
     */
    private String parentFieldName;
    /**
     * 装子元素集合的属性名（例如：children）
     */
    private String childrenFieldName;

    /**
     * 需要处理的一维节点集合
     */
    private List<T> allNodes;

    /**
     * 树形结构的集合
     */
    private List<T> treeNodes;

    /**
     * treeNodes有多少层级
     */
    @Deprecated
    private Integer maxLevel = 1;

    /**
     * 将树形结构按照层级（等级）进行存放在map中，其中key是层级（最大值不超过 level），value是指定层级的树形的
     */
    @Deprecated
    private Map<Integer, List<T>> levelMap;

    /**
     * key:类似自己的id
     * value：key的详细信息，包含其所有子元素
     */
    @Deprecated
    private Map<String, List<T>> selfDetailMap;

    /**
     * 构造方法，参数进行严格限制
     * 并判断其字段名字（selfFieldName、parentFieldName、childrenFieldName）有效性
     * @param selfFieldName
     * @param parentFieldName
     * @param childrenFieldName
     * @param allNodes
     */
    public TreeUtil(@NotBlank String selfFieldName, @NotBlank String parentFieldName, @NotBlank String childrenFieldName, @NotNull @NotEmpty List<T> allNodes) {
        this.selfFieldName = selfFieldName;
        this.parentFieldName = parentFieldName;
        this.childrenFieldName = childrenFieldName;
        this.allNodes = allNodes;
    }

    /**
     * 将没有层级的集合，转变成有层级的集合
     * @return
     */
    private List<T> expand() {

        List<T> parentNodes = new ArrayList<>();
        this.allNodes.stream().forEach(node->{
            try {
                // 对象标识父对象的字段
                Field parentDeclaredField = node.getClass().getDeclaredField(this.parentFieldName);

                // 设置允许访问
                parentDeclaredField.setAccessible(true);

                Object parent = parentDeclaredField.get(node);

                // 对象的父元素字段的值是空，表示该对象是根对象。
                if (parent == null) {
                    parentNodes.add(node);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                log.error("反射异常，具体错误如下：{}", this.selfFieldName, this.parentFieldName, e);
                e.printStackTrace();
            }

        });

        // 无树形结构，直接返回。
        if (parentNodes.size() == this.allNodes.size()) {
            return parentNodes;
        }

        /*
            有树形结构，需要将子元素设置到父元素的 childrenField 属性中去
         */
        childrenHandler(parentNodes);

        this.treeNodes = parentNodes;
        return parentNodes;
    }

    /**
     * 处理父节点的children属性。
     * @param parentNodes
     */
    private void childrenHandler(List<T> parentNodes) {
        // 循环父元素节点们
        parentNodes.stream().forEach(parentNode->{
            try {
                /*
                    获取自己的属性的值，找到其他节点的父
                 */
                Field selfDeclaredField = parentNode.getClass().getDeclaredField(this.selfFieldName);
                selfDeclaredField.setAccessible(true);
                Object self = selfDeclaredField.get(parentNode);

                // 获取子元素
                List<T> children = this.allNodes.stream().filter(f -> {
                    try {
                        Field parentDeclaredField = f.getClass().getDeclaredField(this.parentFieldName);
                        parentDeclaredField.setAccessible(true);
                        Object parent = parentDeclaredField.get(f);
                        return Objects.equals(self, parent);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return false;
                }).collect(Collectors.toList());

                // 设置子元素到父节点的 childrenField 中
                Field childrenDeclaredField = parentNode.getClass().getDeclaredField(this.childrenFieldName);
                childrenDeclaredField.setAccessible(true);
                // 将本次获得子元属添加进去
                childrenDeclaredField.set(parentNode, children);

                // 递归设置子元素
                childrenHandler(children);

            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     *
     * @param selfFieldValue 自己对象标识属性的值（例如：id的值）
     * @return
     */
    private T getTreeNodeBySelfValue(@NotBlank Object selfFieldValue) {
        return this.findBySelfValue(selfFieldValue, this.treeNodes);
    }

    private T findBySelfValue (Object selfFieldValue, List<T> children) {
        if (CollectionUtils.isEmpty(children)) {
            return null;
        }
        Iterator<T> iterator = this.treeNodes.iterator();
        while (iterator.hasNext()) {
            T node = iterator.next();
            try {
                Field declaredField = node.getClass().getDeclaredField(selfFieldName);
                declaredField.setAccessible(true);
                // 为防止类型不匹配导致equals不相等，将其都转成string。
                String nodeSelfValue = Optional.ofNullable((String)declaredField.get(node)).orElse("");
                if (Objects.equals(String.valueOf(selfFieldValue), nodeSelfValue)) {
                    // 匹配本次循环，直接返回
                    return node;
                }
                // node对象不是我们想要的对象，那么就进行递归node的子元素集合进行查找
                Field childrenDeclaredField = node.getClass().getDeclaredField(this.childrenFieldName);
                childrenDeclaredField.setAccessible(true);
                List<T> nodeChildren = Optional.ofNullable((List<T>)childrenDeclaredField.get(node)).orElse(new ArrayList<T>());

                findBySelfValue(selfFieldValue, nodeChildren);


            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        log.error("没有找到您要查找的信息，查找条件：{}", selfFieldValue);
        String clientMessage = StringUtil.format("没有找到您要查找的信息，查找条件：{}", selfFieldValue);
        throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, clientMessage);
    }

    public static void main(String[] args) {
        // 声明5个Menu
        Menu menu1 = new Menu(1, null, null);
        Menu menu2 = new Menu(2, 1, null);
        Menu menu3 = new Menu(3, 1, null);
        Menu menu4 = new Menu(4, 2, null);
        Menu menu5 = new Menu(5, 4, null);

        ArrayList<Menu> menus = Lists.newArrayList(menu1, menu2, menu3, menu4, menu5);

        TreeUtil<Menu> menuTreeUtil = new TreeUtil<>("id", "parentId", "children", menus);

        List<Menu> expand = menuTreeUtil.expand();
        System.out.println(expand);
    }

    @Data
    private static class Menu {
        private Integer id;
        private Integer parentId;
        private List<Menu> children;

        public Menu(Integer id, Integer parentId, List<Menu> children) {
            this.id = id;
            this.parentId = parentId;
            this.children = children;
        }
    }
}
