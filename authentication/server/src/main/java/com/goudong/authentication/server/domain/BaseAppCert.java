package com.goudong.authentication.server.domain;


import cn.zhxu.bs.bean.SearchBean;
import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 * 应用证书表
 * @author cfl
 * @version 1.0
 */
@Entity
@Table(name = "base_app_cert")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Data
public class BaseAppCert extends BasePO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 应用Id
     * insertable = false, updatable = false 外键字段不插入和更新
     */
    @Column(name = "app_id", insertable = false, updatable = false)
    private Long appId;

    /**
     * 证书序号
     */
    @NotNull
    @Column(name = "serial_number", nullable = false, length = 16)
    private String serialNumber;

    /**
     * 证书
     */
    @NotNull
    @Column(name = "cert", nullable = false, length = 1024)
    private String cert;

    /**
     * 私钥
     */
    @NotNull
    @Column(name = "private_key", nullable = false, length = 2048)
    private String privateKey;

    /**
     * 公钥
     */
    @NotNull
    @Column(name = "public_key", nullable = false, length = 1024)
    private String publicKey;

    /**
     * 证书有效截止时间
     */
    @NotNull
    @Column(name = "valid_time", nullable = false)
    private Date validTime;

    /**
     * @JoinColumn(name="app_id") 中app_id是当前实体外键字段
     */
    @ManyToOne
    @JoinColumn(name="app_id")
    private BaseApp baseApp;

    @Override
    public String toString() {
        return "BaseAppCert{" +
                "appId=" + appId +
                ", serialNumber='" + serialNumber + '\'' +
                ", cert='" + cert + '\'' +
                ", privateKey='" + privateKey + '\'' +
                ", publicKey='" + publicKey + '\'' +
                ", validTime=" + validTime +
                '}';
    }
}
