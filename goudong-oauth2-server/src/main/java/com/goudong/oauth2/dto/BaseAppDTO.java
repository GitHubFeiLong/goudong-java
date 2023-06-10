package com.goudong.oauth2.dto;

import com.goudong.oauth2.po.BaseAppPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 应用申请对象
 * @author cfl
 * @version 1.0
 * @date 2023/6/10 22:57
 */
@Data
public class BaseAppDTO {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("id")
    public Long id;

    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private Date createTime;

    /**
     * 创建人id
     */
    @ApiModelProperty("创建人id")
    private Long createUserId;

    /**
     * 更新时间
     */
    @ApiModelProperty("更新时间")
    private Date updateTime;

    /**
     * 更新人id
     */
    @ApiModelProperty("更新人id")
    private Long updateUserId;

    /**
     * 删除状态 0 正常1 删除
     */
    @ApiModelProperty("删除状态")
    protected Boolean deleted;

    @ApiModelProperty("应用id")
    private String appId;

    @ApiModelProperty("密钥")
    private String appSecret;

    /**
     * 应用名
     */
    @ApiModelProperty("应用名")
    private String appName;
    /**
     * 状态
     * @see BaseAppPO.StatusEnum#getId()
     */
    @ApiModelProperty("状态(0:：待审核；1：通过；2：拒绝；)")
    private Integer status;

    /**
     * 备注
     */
    private String remark;
    //~methods
    //==================================================================================================================
}
