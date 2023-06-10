package com.goudong.oauth2.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 类描述：
 * 应用申请对象
 * @author cfl
 * @version 1.0
 * @date 2023/6/10 22:57
 */
@Data
public class BaseAppApplyReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "应用名称", required = true)
    @NotBlank
    private String appName;

    @ApiModelProperty("备注")
    private String remark;
    //~methods
    //==================================================================================================================
}
