package com.goudong.authentication.server.rest.req;

import com.goudong.authentication.server.rest.req.search.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 */
@Data
public class BaseUserPageReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("用户过期时间")
    private Date startValidTime;

    @ApiModelProperty("用户过期时间")
    private Date endValidTime;
}
