package com.goudong.user.po;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

/**
 * 类描述：
 * 角色菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
// @Entity
// @Table(name = "base_role_menu")
public class BaseRoleMenuPO {
    @Id
    @Column(name = "id", nullable = true)
    private Long id;

    /**
     * 角色id
     */
    private Long roleId;

    /**
     * 菜单id
     */
    private Long menuId;
}
