package com.goudong.authentication.server.rest.req.search;

import cn.zhxu.bs.bean.SearchBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 角色下拉分页
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 19:59
 */
@SearchBean(
        tables="base_role"
)
@Data
public class BaseRoleDropDown {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("id")
    private Long id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty(value = "应用名", hidden = true)
    @JsonIgnore
    private Long appId ;

}
