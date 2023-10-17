package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author chenf
 * @version 1.0
 */
@Data
public class CheckPermissionReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("请求uri")
    private String uri;

    @ApiModelProperty("请求方式")
    private String method;

    @ApiModelProperty("token")
    private String token;
    //~methods
    //==================================================================================================================
}
