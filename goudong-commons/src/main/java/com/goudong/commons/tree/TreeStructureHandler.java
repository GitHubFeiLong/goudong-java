package com.goudong.commons.tree;

import com.goudong.commons.enumerate.ClientExceptionEnum;
import com.goudong.commons.exception.ClientException;
import com.goudong.commons.utils.StringUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotBlank;
import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 类描述：
 * 树形结构集合的处理器
 * 1. 将一维节点结构集合转换成树形结构。
 * 2. 获取指定节点及其子节点
 * @author msi
 * @version 1.0
 * @date 2021/9/30 23:27
 */
public class TreeStructureHandler<T> extends AbstractTree<T> {

    private final Logger log = LoggerFactory.getLogger(TreeStructureHandler.class);

    /**
     * 树形结构的集合
     */
    private List<T> treeNodes;


    /**
     * 构造方法，参数进行严格限制
     * 并判断其字段名字（selfFieldName、parentFieldName、childrenFieldName）有效性
     *
     * @param selfFieldName 自己元素标识属性名（例如：id）
     * @param parentFieldName 父元素标识属性名（例如：parentId）
     * @param childrenFieldName 装子元素集合的属性名（例如：children）
     * @param generalNodes 一维节点集合
     */
    public TreeStructureHandler(String selfFieldName, String parentFieldName, String childrenFieldName, List<T> generalNodes) {
        super(selfFieldName, parentFieldName, childrenFieldName, generalNodes);
    }

    /**
     * 将一维node集合转换成树形结构集合
     *
     * @return
     */
    @Override
    public List<T> toTreeStructure() throws NoSuchFieldException, IllegalAccessException {
        // 已经执行过一次该方法，就直接会结果
        if (CollectionUtils.isNotEmpty(this.treeNodes)) {
            return this.treeNodes;
        }

        List<T> parentNodes = new ArrayList<>();
        // 循环一维节点集合
        Iterator<T> iterator = this.allNodes.iterator();
        while (iterator.hasNext()) {
            T node = iterator.next();
            /*
                通过反射获取对象父属性字段的值。
             */
            Field parentDeclaredField = node.getClass().getDeclaredField(this.parentFieldName);
            parentDeclaredField.setAccessible(true);
            Object parent = parentDeclaredField.get(node);

            // 对象的父元素字段的值是空，表示该对象是根对象，将其添加到集合
            if (parent == null) {
                parentNodes.add(node);
            }
        }

        // 无树形结构，直接返回。
        if (parentNodes.size() == this.allNodes.size()) {
            this.treeNodes = parentNodes;
            return parentNodes;
        }

        /*
            有树形结构，需要将子元素设置到父元素的 childrenField 属性中去
         */
        toTreeStructureProcessChildren(parentNodes);

        this.treeNodes = parentNodes;
        return parentNodes;
    }

    /**
     * 处理父节点的children属性。
     * @param parentNodes
     */
    private void toTreeStructureProcessChildren(List<T> parentNodes) throws NoSuchFieldException, IllegalAccessException {
        // 循环父元素节点们
        Iterator<T> iterator = parentNodes.iterator();
        while (iterator.hasNext()) {
            T parentNode = iterator.next();
            /*
                    获取自己的属性的值，找到它的所有子节点。
                 */
            Field selfDeclaredField = parentNode.getClass().getDeclaredField(this.selfFieldName);
            selfDeclaredField.setAccessible(true);
            Object self = selfDeclaredField.get(parentNode);

            // 获取子元素
            List<T> children = this.allNodes.stream().filter(f -> {
                try {
                    // 父元素值（parentId）等于parentNode的属性值（id）,直接返回
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
            toTreeStructureProcessChildren(children);
        }
    }

    /**
     * 将树形结构集合转成一维结构集合
     *
     * @return
     */
    @Override
    public List<T> toGeneralStructure(){
        return this.getGeneralNodes();
    }

    /**
     * 获取指定node的详细信息，包含其所有子node
     *
     * @param selfValue 自己的标识值，例如id
     * @return 根据实现类实现逻辑分为：1. 树形结构的详细信息；2.一维结构的详细信息
     */
    @Override
    public T getNodeDetailBySelfValue(@NotBlank Object selfValue) throws NoSuchFieldException, IllegalAccessException {
        return this.findBySelfValue(selfValue, this.treeNodes);
    }

    /**
     * 递归找到符合条件的node对象
     * @param selfValue
     * @param children
     * @return
     */
    private T findBySelfValue (Object selfValue, List<T> children) throws NoSuchFieldException, IllegalAccessException {
        if (CollectionUtils.isEmpty(children)) {
            return null;
        }
        Iterator<T> iterator = children.iterator();
        while (iterator.hasNext()) {
            T node = iterator.next();
            Field declaredField = node.getClass().getDeclaredField(selfFieldName);
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

            T childNode = findBySelfValue(selfValue, nodeChildren);
            if (childNode != null) {
                return childNode;
            }
        }

        log.error("没有找到您要查找的信息，查找条件：{}", selfValue);
        String clientMessage = StringUtil.format("没有找到您要查找的信息，查找条件：{}", selfValue);
        throw ClientException.clientException(ClientExceptionEnum.BAD_REQUEST, clientMessage);
    }

    /**
     * 获取树形结构集合
     * @return
     */
    public List<T> getTreeNodes() throws NoSuchFieldException, IllegalAccessException {
        return this.toTreeStructure();
    }

    /**
     * 获取一维结构集合
     * @return
     */
    public List<T> getGeneralNodes() {
        return this.allNodes;
    }
}
