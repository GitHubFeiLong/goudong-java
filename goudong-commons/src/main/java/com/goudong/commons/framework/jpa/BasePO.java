package com.goudong.commons.framework.jpa;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 * 通用实体
 * @author msi
 * @date 2021/12/11 20:46
 * @version 1.0
 */
@Data
@MappedSuperclass
// @EntityListeners({AuditingEntityListener.class})
@EntityListeners({DataBaseAuditListener.class})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public abstract class BasePO implements Serializable {

    private static final long serialVersionUID = -2935429857751851837L;

    /**
     * id 生成器
     */
    private static final String ID_GEN = "com.goudong.commons.framework.jpa.MyIdentifierGenerator";

    @Id
    @GenericGenerator(name = "IdGen", strategy = ID_GEN)
    @GeneratedValue(generator = "IdGen")
    public Long id;

    /**
     * 应用id，应用之间进行隔离
     */
    @Column(name = "app_id", nullable = false)
    public Long appId;

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

}
