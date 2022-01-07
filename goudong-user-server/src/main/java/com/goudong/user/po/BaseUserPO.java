package com.goudong.user.po;

import com.goudong.commons.core.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * 类描述：
 * 用户表
 * @author msi
 * @date 2021/12/19 14:20
 * @version 1.0
 */
@Data
@Entity
@Table(name = "base_user")
@SQLDelete(sql = "update base_user set deleted=true where id=?")
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
    private LocalDateTime validTime;

    /**
     * qq登录后，系统获取腾讯的open_id
     */
    @Column(name = "qq_open_id")
    private String qqOpenId;
}
