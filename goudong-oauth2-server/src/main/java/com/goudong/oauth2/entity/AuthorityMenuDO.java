package com.goudong.oauth2.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author msi
 * @Date 2021-04-02 13:50
 * @Version 1.0
 */
@Data
@ApiModel
public class AuthorityMenuDO implements Serializable {
    private static final long serialVersionUID = 1008447951268517432L;
    @ApiModelProperty(value = "菜单表uuid")
    private String uuid;
    /**
     * Ant风格
     */
    @ApiModelProperty(value = "菜单路径")
    private String url;
    @ApiModelProperty(value = "请求方式")
    private String method;
    @ApiModelProperty(value = "菜单名称")
    private String menuName;
    @ApiModelProperty(value = "父级菜单UUID")
    private String parentUuid;
    @ApiModelProperty(value = "备注")
    private String remark;
    @ApiModelProperty(value = "是否被删除")
    private Boolean isDelete;
    @ApiModelProperty(value = "更新时间")
    private Date updateTime;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "角色")
    private List<AuthorityRoleDO> authorityRoleDOS;
}
