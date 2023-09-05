package com.goudong.authentication.server.rest.resp.search;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 19:59
 */
@Data
public class BaseAppPageResp {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("应用id")
    private Long id;

    @ApiModelProperty("应用名称")
    private String name;

    @ApiModelProperty("密钥")
    private String secret;

    @ApiModelProperty("首页地址")
    private String homePage;

    @ApiModelProperty("激活状态")
    private Boolean enabled;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createdDate;
    //~methods
    //==================================================================================================================
}
