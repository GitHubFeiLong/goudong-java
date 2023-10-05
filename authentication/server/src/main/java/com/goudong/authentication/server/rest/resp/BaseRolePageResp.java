package com.goudong.authentication.server.rest.resp;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 角色分页结果
 * @author cfl
 * @version 1.0
 */

@Data
public class BaseRolePageResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "序号")
    private Long serialNumber;

    @ApiModelProperty(value = "应用id")
    private Long appId;

    @ApiModelProperty("角色Id")
    private Long id;

    @ApiModelProperty("角色名")
    private String name;

    @ApiModelProperty("角色备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createdDate;

    @ApiModelProperty("用户")
    private List<BaseUserDropDownResp> users = new ArrayList<>(0);
}
