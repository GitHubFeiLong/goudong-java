package com.goudong.commons.tree;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 树形节点工具封装
 * @author msi
 * @version 1.0
 * @date 2021/9/30 20:35
 */
@Getter
@Slf4j
public abstract class BaseTree<T> implements Tree<T> {

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
     * 树形结构的集合
     */
    protected List<T> treeNodes;


    /**
     * 构造方法，参数进行严格限制
     * 并判断其字段名字（selfFieldName、parentFieldName、childrenFieldName）有效性
     * @param selfFieldName 自己元素标识属性名（例如：id）
     * @param parentFieldName 父元素标识属性名（例如：parentId）
     * @param childrenFieldName 装子元素集合的属性名（例如：children）
     * @param allNodes 所有节点集合，可能是一维节点集合，也可能是树形结构的集合。
     */
    public BaseTree(@NotBlank String selfFieldName, @NotBlank String parentFieldName, @NotBlank String childrenFieldName, @NotNull @NotEmpty List<T> allNodes) {
        this.selfFieldName = selfFieldName;
        this.parentFieldName = parentFieldName;
        this.childrenFieldName = childrenFieldName;
        this.allNodes = allNodes;
    }

    /**
     * 将一维node集合转换成树形结构集合
     * @return
     */
    @Override
    public List<T> toTreeStructure(){
        return null;
    };

    /**
     * 将树形结构集合转成一维结构集合
     * @return
     */
    @Override
    public List<T> toGeneralStructure() {
        return null;
    };

    /**
     * 获取指定node的详细信息，包含其所有子node
     *
     * @param selfValue 自己的标识值，例如id
     * @return 根据实现类实现逻辑分为：1. 树形结构的详细信息；2.一维结构的详细信息
     */
    @Override
    public Object getNodeDetailBySelfValue(Object selfValue) {
        return null;
    }

    public static void main(String[] args) {
        // 声明5个Menu
        Menu menu1 = new Menu(1, null, null);
        Menu menu2 = new Menu(2, 1, null);
        Menu menu3 = new Menu(3, 1, null);
        Menu menu4 = new Menu(4, 2, null);
        Menu menu5 = new Menu(5, 4, null);

        ArrayList<Menu> menus = Lists.newArrayList(menu1, menu2, menu3, menu4, menu5);

        BaseTree<Menu> menuTreeUtil = new TreeStructureHandler<Menu>("id", "parentId", "children", menus);
        List<Menu> expand = menuTreeUtil.toTreeStructure();
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
