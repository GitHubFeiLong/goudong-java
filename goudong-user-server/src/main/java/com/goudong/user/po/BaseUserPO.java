package com.goudong.user.po;

import com.goudong.commons.framework.jpa.BasePO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 用户表
 * @author msi
 * @date 2021/12/19 14:20
 * @version 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "base_user")
@SQLDelete(sql = "update base_user set deleted=null where id=?")
@Where(clause = "deleted=false")
public class BaseUserPO extends BasePO {

    private static final long serialVersionUID = -1209701285445397589L;

    /**
     * 应用id，应用之间进行隔离
     */
    @Column(name = "app_id", nullable = false)
    private Long appId;

    /**
     * 用户名
     */
    @NotBlank(message = "username不能为空")
    @Column(name = "username", nullable = false)
    private String username;

    /**
     * 密码
     */
    @Column(name = "password")
    private String password;

    /**
     * 邮箱
     */
    @Column(name = "email", nullable = false)
    private String email;

    /**
     * 手机号
     */
    @Length(min = 6, max = 32)
    @Column(name = "phone", nullable = false, length = 32)
    private String phone;

    /**
     * 性别（0：未知；1：男；2：女）
     */
    @Column(name = "sex", nullable = false)
    private Integer sex;

    /**
     * 昵称
     */
    @Column(name = "nickname")
    private String nickname;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;

    /**
     * 有效截止时间
     */
    @Column(name = "valid_time")
    private Date validTime;

    /**
     * 头像地址
     */
    @Column(name = "avatar", nullable = false)
    private String avatar;

    /**
     * 激活状态（true：激活；false：未激活）
     */
    @Column(name = "enabled", nullable = true)
    private Boolean enabled;

    /**
     * 锁定状态（true：已锁定；false：未锁定）
     */
    @Column(name = "locked", nullable = true)
    private Boolean locked;

    @ManyToMany(targetEntity=BaseRolePO.class)
    @JoinTable(name = "base_user_role", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns={@JoinColumn(name = "role_id")})
    private List<BaseRolePO> roles = new ArrayList<>();
}
