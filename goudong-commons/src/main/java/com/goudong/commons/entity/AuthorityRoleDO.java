package com.goudong.commons.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @Author msi
 * @Date 2021-04-02 13:49
 * @Version 1.0
 */
@Data
@ApiModel
@Deprecated
public class AuthorityRoleDO {
    @ApiModelProperty(value = "角色表主键uuid")
    private String uuid;
    @ApiModelProperty(value = "角色名称")
    private String roleName;
    @ApiModelProperty(value = "角色名称(中文)")
    private String roleNameCN;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否被删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;


}
