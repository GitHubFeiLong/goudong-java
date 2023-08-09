package com.zhy.authentication.server.domain;


import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 菜单表
 */
@Entity
@Table(name = "base_menu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseMenu extends BasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 父级主键id
     */
    @Column(name = "parent_id")
    private Long parentId;

    /**
     * 应用id
     */
    @NotNull
    @Column(name = "app_id", nullable = false)
    private Long appId;

    /**
     * 权限标识
     */
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "permission_id", length = 64, nullable = false)
    private String permissionId;

    /**
     * 菜单名称
     */
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "name", length = 64, nullable = false)
    private String name;

    /**
     * 菜单类型（1：菜单；2：按钮；3：接口）
     */
    @NotNull
    @Min(value = 1)
    @Max(value = 3)
    @Column(name = "type", nullable = false)
    private Integer type;

    /**
     * 路由或接口地址
     */
    @Size(max = 255)
    @Column(name = "path", length = 255)
    private String path;

    /**
     * 请求方式
     */
    @Column(name = "method")
    private String method;

    /**
     * 排序字段（值越小越靠前，仅仅针对前端路由）
     */
    @Min(value = 1)
    @Max(value = 2147483647)
    @Column(name = "sort_num", nullable = false)
    private Integer sortNum;

    /**
     * 是否是隐藏菜单
     */
    @NotNull
    @Column(name = "hide", nullable = false)
    private Boolean hide;

    /**
     * 前端菜单元数据
     */
    @Size(max = 255)
    @Column(name = "meta", length = 255)
    private String meta;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 创建时间
     */
    @Column(name = "created_date")
    private Date createdDate;

    /**
     * 最后修改时间
     */
    @Column(name = "last_modified_date")
    private Date lastModifiedDate;

    /**
     * 创建人
     */
    @Column(name = "created_by")
    private String createdBy;

    /**
     * 最后修改人
     */
    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @ManyToMany(targetEntity= BaseRole.class, fetch = FetchType.LAZY)
    @JoinTable(name = "base_role_menu", joinColumns = {@JoinColumn(name = "menu_id")},
            inverseJoinColumns={@JoinColumn(name = "role_id")})
    private List<BaseRole> roles = new ArrayList<>();


}
