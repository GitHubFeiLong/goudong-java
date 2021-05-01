package com.goudong.commons.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 不需要登录就能访问的资源
 * @Author msi
 * @Date 2021-04-03 20:19
 * @Version 1.0
 */
@Data
@ApiModel
public class AuthorityIgnoreResourceDO {
    @ApiModelProperty(value = "uuid")
    private String uuid;
    @ApiModelProperty(value = "资源路径")
    private String url;
    @ApiModelProperty(value = "访问资源的方法(多个用逗号分隔)")
    private String method;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否被删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

}
