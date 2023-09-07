package com.goudong.authentication.server.rest.resp;

import com.goudong.authentication.server.rest.req.search.BasePage;
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
public class BaseUserPageResp extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "应用id", hidden = true)
    private Long appId;

    @ApiModelProperty(value = "应用名", hidden = true)
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
    private List<String> roles = new ArrayList<>(0);
}
