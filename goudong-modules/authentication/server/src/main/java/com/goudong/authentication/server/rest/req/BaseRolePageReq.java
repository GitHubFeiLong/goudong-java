package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.rest.req.search.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseRolePageReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("角色id")
    private List<Long> ids;

    @ApiModelProperty("角色名称")
    private String name;

    @ApiModelProperty("角色备注")
    private String remark;
}
