package com.goudong.commons.tree.example;

import com.google.common.collect.Lists;
import com.goudong.commons.tree.GeneralNode;
import com.goudong.commons.tree.GeneralStructureHandler;
import com.goudong.commons.tree.TreeStructureHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/10/1 15:58
 */
public class Example {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        // 声明5个Menu
        Menu menu1 = new Menu(1, null, null);
        Menu menu2 = new Menu(2, 1, null);
        Menu menu3 = new Menu(3, 1, null);
        Menu menu4 = new Menu(4, 2, null);
        Menu menu5 = new Menu(5, 4, null);

        ArrayList<Menu> menus = Lists.newArrayList(menu1, menu2, menu3, menu4, menu5);
        System.out.println("准备的参数 menus = " + menus);

        /*
            构造函数参数不能出错。
            根据需求，创建不同的实例对象
                TreeStructureHandler：树形相关的处理器
                GeneralStructureHandler：一维结构的处理器
         */
        /*
            树
         */
        TreeStructureHandler<Menu> treeStructureHandler = new TreeStructureHandler<Menu>("id", "parentId", "children", menus);
        // 获取转换后的树形结构（树）
        List<Menu> expand = treeStructureHandler.toTreeStructure();
        System.out.println("expand = " + expand);
        // 获取一维结构集合（一般）
        List<Menu> toGeneralStructure = treeStructureHandler.toGeneralStructure();
        System.out.println("toGeneralStructure = " + toGeneralStructure);
        // 获取指定节点详细信息（一般）
        GeneralNode<Menu> nodeDetailBySelfValue2GeneralNode = treeStructureHandler.getNodeDetailBySelfValue2GeneralNode(2);
        System.out.println("nodeDetailBySelfValue2GeneralNode = " + nodeDetailBySelfValue2GeneralNode);
        // 获取指定节点详细信息(树)
        Menu nodeDetailBySelfValue = treeStructureHandler.getNodeDetailBySelfValue2T(2);
        System.out.println("nodeDetailBySelfValue = " + nodeDetailBySelfValue);

        System.out.println("====分割线====");
        /*
            一般
         */
        GeneralStructureHandler<Menu> menuGeneralStructureHandler = new GeneralStructureHandler<>("id", "parentId", "children", expand);
        // 获取转换后的树形结构（树）
        List<Menu> toTreeStructure = menuGeneralStructureHandler.toTreeStructure();
        System.out.println("toTreeStructure = " + toTreeStructure);
        // 获取一维结构集合（一般）
        List<Menu> toGeneralStructure1 = menuGeneralStructureHandler.toGeneralStructure();
        System.out.println("toGeneralStructure1 = " + toGeneralStructure1);
        // 获取指定节点详细信息（一般）
        GeneralNode<Menu> nodeDetailBySelfValue2GeneralNode1 = menuGeneralStructureHandler.getNodeDetailBySelfValue2GeneralNode(2);
        System.out.println("nodeDetailBySelfValue2GeneralNode1 = " + nodeDetailBySelfValue2GeneralNode1);
        // 获取指定节点详细信息(树)
        Menu nodeDetailBySelfValue2T = menuGeneralStructureHandler.getNodeDetailBySelfValue2T(2);
        System.out.println("nodeDetailBySelfValue2T = " + nodeDetailBySelfValue2T);


        System.out.println(1);
    }
}
