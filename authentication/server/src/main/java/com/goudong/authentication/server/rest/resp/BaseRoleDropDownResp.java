package com.goudong.authentication.server.rest.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 类描述：
 * 角色下拉
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 19:59
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseRoleDropDownResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色名")
    private String name;
}
