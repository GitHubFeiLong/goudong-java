package com.goudong.authentication.server.domain;


import cn.zhxu.bs.bean.SearchBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 证书
     * 级联保存、更新、删除、刷新;延迟加载。当删除应用，会级联删除该应用的所有证书
     * 拥有mappedBy注解的实体类为关系被维护端
     * mappedBy="baseApp"中的baseApp是BaseAppCert中的baseApp属性
     */
    @OneToMany(mappedBy = "baseApp", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BaseAppCert> certs = new ArrayList<>();

    @Override
    public String toString() {
        return "BaseApp{" +
                "secret='" + secret + '\'' +
                ", name='" + name + '\'' +
                ", homePage='" + homePage + '\'' +
                ", enabled=" + enabled +
                ", remark='" + remark + '\'' +
                '}';
    }
}
