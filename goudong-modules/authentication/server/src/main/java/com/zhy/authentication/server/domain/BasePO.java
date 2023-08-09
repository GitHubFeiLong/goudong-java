package com.zhy.authentication.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zhy.authentication.server.config.DataBaseAuditListener;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/17 19:06
 */
@Data
@MappedSuperclass
@EntityListeners({DataBaseAuditListener.class})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public abstract class BasePO {

    /**
     * id 生成器
     */
    private static final String ID_GEN = "com.zhy.authentication.server.config.MyIdentifierGenerator";

    //~fields
    //==================================================================================================================
    @Id
    @GenericGenerator(name = "IdGen", strategy = ID_GEN)
    @GeneratedValue(generator = "IdGen")
    private Long id;

    /**
     * 创建时间
    */
    @CreatedDate
    @Column(name = "created_date")
    @JsonIgnore
    private Date createdDate;

    /**
     * 最后修改时间
     */
    @LastModifiedDate
    @Column(name = "last_modified_date")
    @JsonIgnore
    private Date lastModifiedDate;

    /**
     * 创建人
     */
    @CreatedBy
    @Column(name = "created_by")
    @JsonIgnore
    private String createdBy;

    /**
     * 最后修改人
     */
    @LastModifiedBy
    @Column(name = "last_modified_by")
    @JsonIgnore
    private String lastModifiedBy;
    //~methods
    //==================================================================================================================
}
