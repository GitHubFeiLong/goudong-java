package com.goudong.authentication.server.rest.resp;

import io.swagger.annotations.ApiModelProperty;

/**
 * 类描述：
 * 用户导入响应
 * @author chenf
 * @version 1.0
 */
@Deprecated
public class BaseImportResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "是否处理成功", required = true)
    private Boolean successful;

    @ApiModelProperty(value = "失败信息或成功信息")
    private String message;
    //~methods
    //==================================================================================================================
}
