package com.goudong.authentication.server.rest.req.search;

import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.DbIgnore;
import cn.zhxu.bs.bean.SearchBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 19:59
 */
@SearchBean(
        tables="base_user bu inner join base_app ba on ba.id = bu.app_id",
        orderBy = "ba.id desc, bu. created_date desc"
)
@Data
public class BaseUserPage extends BasePage {
    //~fields
    //==================================================================================================================

    @ApiModelProperty(value = "应用id")
    @DbField(value = "ba.id")
    private Long appId;

    @ApiModelProperty("应用名")
    @DbField("ba.name")
    private String appName;

    @ApiModelProperty("用户名")
    @DbField("bu.id")
    private Long id;

    @ApiModelProperty("用户名")
    @DbField("bu.username")
    private String username;

    @ApiModelProperty("用户过期时间")
    @DbField("bu.valid_time")
    private Date validTime;

    @ApiModelProperty("激活状态")
    @DbField("bu.enabled")
    private Boolean enabled;

    @ApiModelProperty("锁定状态")
    @DbField("bu.locked")
    private Boolean locked;

    @ApiModelProperty("备注")
    @DbField("bu.remark")
    private String remark;

    @ApiModelProperty("创建时间")
    @DbField("bu.created_date")
    private Date createdDate;

    @ApiModelProperty("角色")
    @DbIgnore
    private List<SelectUsersRoleNames> roles = new ArrayList<>(0);
}
