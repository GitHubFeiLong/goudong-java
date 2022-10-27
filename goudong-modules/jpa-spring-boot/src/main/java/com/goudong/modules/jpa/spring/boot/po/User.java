package com.goudong.modules.jpa.spring.boot.po;

import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 类描述：
 * 用户表
 * @author cfl
 * @version 1.0
 * @date 2022/10/27 14:06
 */
@Data
@Entity
@Table(name = "user")
@SQLDelete(sql = "update user set deleted=true where id=?")
@Where(clause = "deleted=false")
public class User {

    @Id
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

    private String name;
}
