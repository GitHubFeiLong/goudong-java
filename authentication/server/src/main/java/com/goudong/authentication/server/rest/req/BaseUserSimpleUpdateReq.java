package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 修改用户
 */
@Data
public class BaseUserSimpleUpdateReq implements Serializable {

    /**
     * 用户id
     */
    @NotNull
    @ApiModelProperty(value = "用户id", required = true)
    private Long id;

    @NotNull
    @NotEmpty
    @ApiModelProperty(value = "角色", required = true)
    private List<Long> roleIds;

    /**
     * 有效截止时间
     */
    @ApiModelProperty(value = "有效截止时间")
    private Date validTime;

    /**
     * 激活状态：true 激活；false 未激活
     */
    @ApiModelProperty(value = "激活状态：true 激活；false 未激活")
    private Boolean enabled;

    /**
     * 锁定状态：true 锁定；false 未锁定
     */
    @ApiModelProperty(value = "锁定状态：true 锁定；false 未锁定")
    private Boolean locked;

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;
}
