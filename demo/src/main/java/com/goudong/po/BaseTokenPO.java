package com.goudong.po;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：
 * 令牌
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:21
 */
@Data
@Entity
@Table(name = "base_token")
@SQLDelete(sql = "update base_token set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseTokenPO {
    private static final long serialVersionUID = -5455706615076826311L;

    /**
     * id 生成器
     */
    private static final String ID_GEN = "com.goudong.MyIdentifierGenerator";

    @Id
    @GenericGenerator(name = "IdGen", strategy = ID_GEN)
    @GeneratedValue(generator = "IdGen")
    public Long id;

    /**
     * 创建时间
     */
    @CreatedDate
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 创建人id
     */
    @CreatedBy
    @Column(name = "create_user_id")
    private Long createUserId;

    /**
     * 更新时间
     */
    @LastModifiedDate
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 更新人id
     */
    @LastModifiedBy
    @Column(name = "update_user_id")
    private Long updateUserId;

    /**
     * 删除状态 0 正常1 删除
     */
    @Column(name = "deleted")
    protected Boolean deleted;

    //~fields
    //==================================================================================================================
    /**
     * 访问令牌
     */
    @NotBlank
    private String accessToken;
    /**
     * 刷新令牌
     */
    @NotBlank
    private String refreshToken;
    /**
     * accessToken失效时长
     */
    @NotNull
    private Date accessExpires;
    /**
     * refreshToken失效时长
     */
    @NotNull
    private Date refreshExpires;
    /**
     * 用户id
     */
    @NotNull
    private Long userId;
    /**
     * 客户端类型
     */
    @NotNull
    private String clientType;

    //~methods
    //==================================================================================================================

}