package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：
 * 新增应用证书
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseAppCertCreateReq {
    //~fields
    //==================================================================================================================
    /**
     * 应用id
     */
    @NotNull
    @ApiModelProperty(value = "应用id", required = true)
    private Long appId;

    /**
     * 应用名称
     */
    @ApiModelProperty(value = "应用名称(不传时，使用应用原名称)", required = false)
    private String appName;

    @NotNull
    @ApiModelProperty(value = "有效期", required = true)
    private Date validTime;
    //~methods
    //==================================================================================================================
}
