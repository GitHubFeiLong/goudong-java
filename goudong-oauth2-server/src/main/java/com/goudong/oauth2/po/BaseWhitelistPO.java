package com.goudong.oauth2.po;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * 类描述：
 * 白名单
 * @author msi
 * @version 1.0
 * @date 2022/1/8 16:46
 */
@Data
@Entity
@Table(name = "base_whitelist")
@SQLDelete(sql = "update base_whitelist set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseWhitelistPO{

    @Id
    @GeneratedValue
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

    /**
     * 匹配模式
     */
    private String pattern;
    /**
     * 请求的方法
     */
    private String methods;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否是系统设置的
     */
    private Boolean isSystem = false;

    /**
     * 是否只能内部服务调用
     */
    private Boolean isInner = false;

    /**
     * 是否关闭该白名单
     */
    private Boolean isDisable = false;
}
