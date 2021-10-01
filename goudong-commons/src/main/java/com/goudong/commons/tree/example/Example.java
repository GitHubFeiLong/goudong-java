package com.goudong.commons.tree.example;

import com.google.common.collect.Lists;
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

        /*
            构造函数参数不能出错。
            根据需求，创建不同的实例对象
                TreeStructureHandler：树形相关的处理器
                GeneralStructureHandler：一维结构的处理器
         */
        TreeStructureHandler<Menu> treeStructureHandler = new TreeStructureHandler<Menu>("id", "parentId", "children", menus);
        // 获取转换后的树形结构 功能同 treeStructureHandler.getTreeNodes()一样
        List<Menu> expand = treeStructureHandler.toTreeStructure();
        // 获取转换后的树形结构中的指定节点及其所有子节点
        Menu nodeDetailBySelfValue = treeStructureHandler.getNodeDetailBySelfValue(2);
        // 创建对象时构造方法传递一维结构集合
        List<Menu> generalNodes = treeStructureHandler.getGeneralNodes();
        // 创建对象时构造方法传递一维结构集合
        List<Menu> toGeneralStructure = treeStructureHandler.toGeneralStructure();

        GeneralStructureHandler<Menu> menuGeneralStructureHandler = new GeneralStructureHandler<>("id", "parentId", "children", expand);
        List<Menu> generalStructure = menuGeneralStructureHandler.getGeneralNodes();
        GeneralStructureHandler.GeneralNode<Menu> nodeDetailBySelfValue1 = menuGeneralStructureHandler.getNodeDetailBySelfValue(2);


        System.out.println(1);
    }
}
