package com.goudong.authentication.server.rest.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 角色下拉分页
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseUserDropDownResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String name;
}


