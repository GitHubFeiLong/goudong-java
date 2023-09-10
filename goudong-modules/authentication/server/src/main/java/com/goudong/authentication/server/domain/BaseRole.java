package com.goudong.authentication.server.domain;


import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色表
 */
@Entity
@Table(name = "base_role")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseRole extends BasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 应用id
     */
    @NotNull
    @Column(name = "app_id", nullable = false)
    private Long appId;

    /**
     * 角色名称
     */
    @NotNull
    @Size(min = 4, max = 16)
    @Column(name = "name", length = 16, nullable = false)
    private String name;

    /**
     * 备注
     */
    @Size(max = 255)
    @Column(name = "remark")
    private String remark;

    @ManyToMany(targetEntity=BaseUser.class, fetch = FetchType.LAZY)
    @JoinTable(name = "base_user_role", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns={@JoinColumn(name = "user_id")})
    private List<BaseUser> users = new ArrayList<>();

    @ManyToMany(targetEntity=BaseMenu.class, fetch = FetchType.LAZY)
    @JoinTable(name = "base_role_menu", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns={@JoinColumn(name = "menu_id")})
    private List<BaseMenu> menus = new ArrayList<>();

    @Override
    public String toString() {
        return "BaseRole{" +
                "id=" + id +
                ", appId=" + appId +
                ", name='" + name + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
