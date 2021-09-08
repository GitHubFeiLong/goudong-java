package com.goudong.oauth2.mapstruct.test;

import com.google.common.collect.Lists;
import com.goudong.oauth2.mapstruct.mappser.MenuMapper;
import com.goudong.oauth2.mapstruct.po.MenuDO;
import com.goudong.oauth2.mapstruct.po.MenuVO;
import org.junit.Test;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/9/9 0:03
 */
// @SpringBootTest
public class MapperTest {

    @Test
    public void test1 () {
        MenuDO menuDO = new MenuDO();
        menuDO.setMenuName("menuName");
        menuDO.setUrl("url");
        MenuDO menuDO1 = new MenuDO();
        menuDO1.setMenuName("menuName");
        menuDO1.setUrl("url");
        menuDO1.setSubMenus(Lists.newArrayList(menuDO1));

        MenuVO vo = MenuMapper.INSTANCE.do2Vo(menuDO);

        System.out.println("vo = " + vo);
    }

    public static void main(String[] args) {
        MenuDO menuDO = new MenuDO();
        menuDO.setMenuName("menuName");
        menuDO.setUrl("url");
        MenuDO menuDO1 = new MenuDO();
        menuDO1.setMenuName("111 menuName");
        menuDO1.setUrl("111 url");

        MenuDO menuDO2 = new MenuDO();
        menuDO2.setMenuName("222 menuName");
        menuDO2.setUrl("222 url");

        MenuDO menuDO3 = new MenuDO();
        menuDO3.setMenuName("menuDO3 menuName");
        menuDO3.setUrl("menuDO3 url");

        MenuDO menuDO4 = new MenuDO();
        menuDO4.setMenuName("menuDO4 menuName");
        menuDO4.setUrl("menuDO4 url");

        MenuDO menuDO5 = new MenuDO();
        menuDO5.setMenuName("menuDO5 menuName");
        menuDO5.setUrl("menuDO5 url");

        menuDO3.setSubMenus(Lists.newArrayList(menuDO4, menuDO5));

        menuDO.setSubMenus(Lists.newArrayList(menuDO1, menuDO2, menuDO3));

        MenuVO vo = MenuMapper.INSTANCE.do2Vo(menuDO);

        System.out.println("vo = " + vo);
    }
}
