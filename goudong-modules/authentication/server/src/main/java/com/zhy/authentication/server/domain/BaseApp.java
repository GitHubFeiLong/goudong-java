package com.zhy.authentication.server.domain;


import cn.zhxu.bs.bean.SearchBean;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 应用表
 */
@SearchBean(tables="base_app ba")
@Entity
@Table(name = "base_app")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseApp extends BasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用密钥
     */
    @NotNull
    @Size(max = 64)
    @Column(name = "secret", length = 64, nullable = false)
    private String secret;

    /**
     * 应用名称
     */
    @NotNull
    @Size(max = 16)
    @Column(name = "name", length = 16, nullable = false)
    private String name;

    /**
     * 应用首页
     */
    @Size(max = 255)
    @Column(name = "home_page", length = 255, nullable = true)
    private String homePage;

    /**
     * 是否激活
     */
    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;
    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}
