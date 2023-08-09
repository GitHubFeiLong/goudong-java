package com.zhy.authentication.server.service.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * A DTO for the {@link com.zhy.authentication.server.domain.BaseUser} entity.
 */
@ApiModel(description = "基础用户")
@Data
public class BaseUserDTO implements Serializable {

    private Long id;

    /**
     * 应用id
     */
    @NotNull
    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;

    /**
     * 用户名
     */
    @NotNull
    @Size(max = 16)
    @ApiModelProperty(value = "用户名", required = true)
    private String username;

    /**
     * 密码
     */
    @NotNull
    @Size(max = 128)
    @ApiModelProperty(value = "密码", required = true)
    private String password;

    /**
     * 激活状态：true 激活；false 未激活
     */
    @NotNull
    @ApiModelProperty(value = "激活状态：true 激活；false 未激活", required = true)
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    @NotNull
    @ApiModelProperty(value = "锁定状态：true 锁定；false 未锁定", required = true)
    private Boolean locked;

    /**
     * 有效截止时间
     */
    @NotNull
    @ApiModelProperty(value = "有效截止时间", required = false)
    private Date validTime;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

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
}
