package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 修改菜单排序请求参数
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseMenuChangeSortNumReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "移动的菜单id", required = true)
    @NotNull
    private Long beforeId;

    @ApiModelProperty(value = "移动后所处的菜单id", required = true)
    @NotNull
    private Long afterId;
    //~methods
    //==================================================================================================================
}
