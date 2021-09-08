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
        menuDO.setSubMenus(Lists.newArrayList(menuDO1));

        MenuVO vo = MenuMapper.INSTANCE.do2Vo(menuDO);

        System.out.println("vo = " + vo);
    }
}
