package com.goudong.commons.tree.stream;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.goudong.commons.tree.example.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.Collectors;

/**
 * 类描述：
 *
 * @Author e-Feilong.Chen
 * @Date 2022/4/13 15:05
 */
public class Demo {
    //~fields
    //==================================================================================================================

    //~construct methods
    //==================================================================================================================

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
        List<Menu> menus = new Tree<Menu>().toTree(list1);
        System.out.println("JSON.toJSONString(menus) = " + JSON.toJSONString(menus));
    }
    // public static void main(String[] args) {
    //     // 第一棵树
    //     Menu menu1 = new Menu(1, null, null);
    //     Menu menu2 = new Menu(2, 1, null);
    //     Menu menu3 = new Menu(3, 1, null);
    //     Menu menu4 = new Menu(4, 2, null);
    //     Menu menu5 = new Menu(5, 4, null);
    //     ArrayList<Menu> list1 = Lists.newArrayList(menu1, menu2, menu3, menu4, menu5);
    //
    //     // 第二棵树
    //     Menu menu6 = new Menu(6, null, null);
    //     Menu menu7 = new Menu(7, 6, null);
    //     Menu menu8 = new Menu(8, 7, null);
    //     Menu menu9 = new Menu(9, 8, null);
    //     ArrayList<Menu> list2 = Lists.newArrayList(menu6, menu7, menu8, menu9);
    //
    //     List<Menu> collect = list1.stream().filter(f -> f.getParentId() == null)
    //             .map(m -> {
    //                 m.setChildren(getChildren(m, list1));
    //                 return m;
    //             }).collect(Collectors.toList());
    //     System.out.println("collect = " + JSON.toJSONString(collect));
    // }
    //
    // public static List<Menu> getChildren(Menu menu, List<Menu> menus){
    //     return menus.stream().filter(f->{
    //         return Objects.equals(f.getParentId(), menu.getId());
    //     }).map(m->{
    //         m.setChildren(getChildren(m, menus));
    //         return m;
    //     }).collect(Collectors.toList());
    // }
}
