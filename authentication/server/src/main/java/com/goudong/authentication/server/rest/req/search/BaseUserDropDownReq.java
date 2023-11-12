package com.goudong.authentication.server.rest.req.search;

import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.SearchBean;
import cn.zhxu.bs.operator.Contain;
import cn.zhxu.bs.operator.Equal;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 角色下拉分页
 * @author cfl
 * @version 1.0
 */
@SearchBean(tables="base_user", orderBy = "username asc")
@Data
public class BaseUserDropDownReq extends BasePage{
    //~fields
    //==================================================================================================================
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    @DbField(value = "username", onlyOn = Contain.class)
    private String name;

    @ApiModelProperty(value = "应用名", hidden = true)
    @DbField(value = "real_app_id", onlyOn = Equal.class)
    @JsonIgnore
    private Long realAppId ;
}
