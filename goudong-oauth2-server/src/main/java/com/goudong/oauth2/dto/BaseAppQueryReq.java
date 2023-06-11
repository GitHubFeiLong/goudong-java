package com.goudong.oauth2.dto;

import com.goudong.oauth2.po.BaseAppPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 应用查询对象
 * @author cfl
 * @version 1.0
 * @date 2023/6/10 22:57
 */
@Data
public class BaseAppQueryReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "应用名称")
    private String appName;

    /**
     * @see BaseAppPO.StatusEnum#getId()
     */
    @ApiModelProperty(value = "状态（0：待审核；1：通过；2：拒绝；）")
    private Integer status;
    //~methods
    //==================================================================================================================
}
