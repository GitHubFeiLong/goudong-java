package com.goudong.authentication.server.domain;


import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 菜单角色中间表
 */
@Entity
@Table(name = "base_role_menu")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseRoleMenu extends BasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @ManyToOne
    // @JsonIgnoreProperties("menus")
    // private BaseRole role;

    // @ManyToOne
    // @JsonIgnoreProperties("roles")
    // private BaseMenu menu;
}
