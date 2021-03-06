package com.goudong.user.po;

import com.goudong.commons.frame.jpa.BasePO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
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
     * qq登录后，系统获取腾讯的open_id
     */
    @Column(name = "qq_open_id")
    private String qqOpenId;

    @ManyToMany(targetEntity=BaseRolePO.class)
    @JoinTable(name = "base_user_role", joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns={@JoinColumn(name = "role_id")})
    private List<BaseRolePO> roles = new ArrayList<>();
}
