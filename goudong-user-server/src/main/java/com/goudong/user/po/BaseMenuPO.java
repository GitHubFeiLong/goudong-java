package com.goudong.user.po;

import com.goudong.commons.framework.jpa.BasePO;
import com.goudong.core.lang.IEnum;
import lombok.Data;
import lombok.Getter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.http.HttpMethod;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
@Entity
@Table(name = "base_menu")
@SQLDelete(sql = "update base_menu set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseMenuPO extends BasePO {
    private static final long serialVersionUID = -6254288573268456187L;

    /**
     * 应用id
     */
    private Long appId;
    /**
     * 父菜单主键
     */
    private Long parentId;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 菜单类型（0：接口；1：菜单；2：按钮）
     */
    private Integer type;

    /**
     * 打开方式（0：内链；1：外链）
     */
    private Integer openModel;

    /**
     * 前端的路由或后端的接口，
     */
    private String path;

    /**
     * 前端组件地址
     */
    private String componentPath;

    /**
     * 请求方式,值为{@link HttpMethod}元素的name(),例如：GET，POST。
     * 这里path 和 method 是一对一的方式，方便更细粒度鉴权。
     */
    private String method;

    /**
     * 图标
     */
    private String icon;

    /**
     * 权限标识（前端的菜单和按钮需要）
     */
    private String permissionId;

    /**
     * 排序字段（值越小越靠前，仅仅针对前端路由）
     */
    private Integer sortNum;

    /**
     * 是否是隐藏菜单
     */
    private Boolean hide;

    /**
     * 前端菜单组件的信息
     */
    private String metadata;
    /**
     * 备注
     */
    private String remark;

    @ManyToMany(targetEntity= BaseRolePO.class, fetch = FetchType.EAGER)
    @JoinTable(name = "base_role_menu", joinColumns = {@JoinColumn(name = "menu_id")},
            inverseJoinColumns={@JoinColumn(name = "role_id")})
    private List<BaseRolePO> roles = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BaseMenuPO po = (BaseMenuPO) o;
        return Objects.equals(parentId, po.parentId) && Objects.equals(name, po.name) && Objects.equals(type, po.type) && Objects.equals(openModel, po.openModel) && Objects.equals(path, po.path) && Objects.equals(method, po.method) && Objects.equals(icon, po.icon) && Objects.equals(permissionId, po.permissionId) && Objects.equals(sortNum, po.sortNum) && Objects.equals(hide, po.hide) && Objects.equals(metadata, po.metadata) && Objects.equals(remark, po.remark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), parentId, name, type, openModel, path, method, icon, permissionId, sortNum, hide, metadata, remark);
    }

    /**
     * 接口描述：
     * 菜单的类型美剧
     * @author Administrator
     * @version 1.0
     * @date 2023/6/4 20:04
     */
    @Getter
    public enum TypeEnum implements IEnum<Integer, TypeEnum> {
        API_INTERFACE(0, "接口"),
        MENU(1, "菜单"),
        BUTTON(2, "按钮"),
        ;
        private Integer id;

        private String label;
        //~methods
        //==================================================================================================================
        TypeEnum(Integer id, String label) {
            this.id = id;
            this.label = label;
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
        public TypeEnum getById(Integer id) {
            return IEnum.super.getById(id);
        }
    }

    /**
     * 接口描述：
     * 菜单的类型美剧
     * @author Administrator
     * @version 1.0
     * @date 2023/6/4 20:04
     */
    @Getter
    public enum OpenModelEnum implements IEnum<Integer, OpenModelEnum> {
        INNER_LINK(0, "内链"),
        OUTER_LINK(1, "外链"),
        ;
        private Integer id;

        private String label;
        //~methods
        //==================================================================================================================
        OpenModelEnum(Integer id, String label) {
            this.id = id;
            this.label = label;
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
        public OpenModelEnum getById(Integer id) {
            return IEnum.super.getById(id);
        }
    }
}
