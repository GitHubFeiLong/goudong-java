package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

/**
 * 修改用户
 */
@Data
public class BaseUserUpdate implements Serializable {

    /**
     * 用户id
     */
    @NotNull
    @ApiModelProperty(value = "用户id", required = true)
    private Long id;

    /**
     * 密码
     */
    @Size(max = 32)
    @ApiModelProperty(value = "密码")
    private String password;

    /**
     * 备注
     */
    @Size(max = 255)
    @ApiModelProperty(value = "备注")
    private String remark;

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
     * 有效截止时间
     */
    @ApiModelProperty(value = "有效截止时间")
    private Date validTime;

}
