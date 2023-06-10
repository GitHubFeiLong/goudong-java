package com.goudong.oauth2.dto;

import com.goudong.oauth2.po.BaseAppPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 应用审核对象
 * @author cfl
 * @version 1.0
 * @date 2023/6/10 22:57
 */
@Data
public class BaseAppAuditReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "id", required = true)
    @NotNull
    private Long id;

    /**
     * @see BaseAppPO.StatusEnum#getId()
     */
    @ApiModelProperty(value = "状态", required = true)
    @NotNull
    private Integer status;

    @ApiModelProperty("备注")
    private String remark;
    //~methods
    //==================================================================================================================
}
