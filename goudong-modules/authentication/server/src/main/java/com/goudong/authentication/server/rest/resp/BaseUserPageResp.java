package com.goudong.authentication.server.rest.resp;

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

@Data
public class BaseUserPageResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "序号")
    private Long serialNumber;

    @ApiModelProperty(value = "应用id")
    private Long appId;

    @ApiModelProperty(value = "应用名")
    private String appName;

    @ApiModelProperty("用户名")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户过期时间")
    private Date validTime;

    @ApiModelProperty("激活状态")
    private Boolean enabled;

    @ApiModelProperty("锁定状态")
    private Boolean locked;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createdDate;

    @ApiModelProperty("角色")
    private List<BaseRoleDropDownResp> roles = new ArrayList<>(0);
}
