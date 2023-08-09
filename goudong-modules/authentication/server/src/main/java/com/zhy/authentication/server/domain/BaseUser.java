package com.zhy.authentication.server.domain;


import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CollectionId;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 基础用户
 */
@Data
@Entity
@Table(name = "base_user")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BaseUser extends BasePO implements Serializable {

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
     * 真实应用id（例如xx应用管理员，app_id是认证服务应用的app_id，但是real_app_id是自己所管理xx应用的app_id）
     */
    @NotNull
    @Column(name = "real_app_id", nullable = false)
    private Long realAppId;

    /**
     * 用户名
     */
    @NotNull
    @Size(max = 16)
    @Column(name = "username", length = 16, nullable = false)
    private String username;

    /**
     * 密码
     */
    @NotNull
    @Size(max = 64)
    @Column(name = "password", length = 64, nullable = false)
    private String password;

    /**
     * 激活状态：true 激活；false 未激活
     */
    @NotNull
    @Column(name = "enabled", nullable = false)
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    @NotNull
    @Column(name = "locked", nullable = false)
    private Boolean locked;

    /**
     * 有效截止时间
     */
    @Column(name = "valid_time", nullable = false)
    private Date validTime;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    @ManyToMany(targetEntity= BaseRole.class, fetch = FetchType.LAZY)
    @JoinTable(name = "base_user_role", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns={@JoinColumn(name = "role_id")})
    // @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private List<BaseRole> roles = new ArrayList<>();

    @Override
    public String toString() {
        return "BaseUser{" +
                "id=" + id +
                ", appId=" + appId +
                ", realAppId=" + realAppId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", enabled=" + enabled +
                ", locked=" + locked +
                ", validTime=" + validTime +
                ", remark='" + remark + '\'' +
                '}';
    }
}
