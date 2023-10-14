package com.goudong.authentication.server.service.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：
 * 应用证书
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseAppCertDTO {
    //~fields
    //==================================================================================================================
    /**
     * 应用证书Id
     */
    @ApiModelProperty(value = "应用证书Id")
    private Long id;
    /**
     * 应用Id
     */
    @ApiModelProperty(value = "应用Id")
    private Long appId;

    /**
     * 证书序号
     */
    @ApiModelProperty(value = "证书序号")
    private String serialNumber;

    /**
     * 证书
     */
    @ApiModelProperty(value = "证书")
    private String cert;

    /**
     * 私钥
     */
    @ApiModelProperty(value = "私钥")
    private String privateKey;

    /**
     * 公钥
     */
    @ApiModelProperty(value = "公钥")
    private String publicKey;

    /**
     * 证书有效截止时间
     */
    @ApiModelProperty(value = "证书有效截止时间")
    private Date validTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    private Date createdDate;

    /**
     * 最后修改时间
     */
    @ApiModelProperty(value = "最后修改时间")
    private Date lastModifiedDate;

    /**
     * 创建人
     */
    @ApiModelProperty(value = "创建人")
    private String createdBy;

    /**
     * 最后修改人
     */
    @ApiModelProperty(value = "最后修改人")
    private String lastModifiedBy;
    //~methods
    //==================================================================================================================
}
