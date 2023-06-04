package com.goudong.user.enumerate.menu;

import com.goudong.core.lang.IEnum;
import lombok.Getter;

/**
 * 接口描述：
 * 菜单的类型美剧
 * @author Administrator
 * @version 1.0
 * @date 2023/6/4 20:04
 */
@Getter
public enum BaseMenuTypeEnum implements IEnum<Integer, BaseMenuTypeEnum> {
    // （0：接口；1：菜单；2：按钮）
    API_INTERFACE(0),
    menu(1),
    BUTTON(2),
        ;
    private Integer id;
    //~methods
    //==================================================================================================================
    BaseMenuTypeEnum(Integer id) {
        this.id = id;
    }

    public static void main(String[] args) {
        BaseMenuTypeEnum byId = BaseMenuTypeEnum.API_INTERFACE.getById(2);
        System.out.println("byId = " + byId);
    }
    /**
     * 获取枚举成员的唯一标识
     *
     * @return
     */
    @Override
    public Integer getId() {
        return id;
    }

    /**
     * 根据{@code id} 找到对应的枚举成员并返回<br>
     *
     * @param id 待找的枚举成员id
     * @return
     */
    @Override
    public BaseMenuTypeEnum getById(Integer id) {
        return IEnum.super.getById(id);
    }
}
