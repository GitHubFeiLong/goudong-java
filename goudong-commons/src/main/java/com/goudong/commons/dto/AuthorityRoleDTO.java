package com.goudong.commons.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 角色 DTO 传输层对象
 * authority_role
 * @author
 */
@Data
public class AuthorityRoleDTO implements Serializable {
    /**
     * uuid
     */
    private String uuid;

    /**
     * 角色名称(必须以ROLE_起始命名)
     */
    private String roleName;

    /**
     * 角色名称中文
     */
    private String roleNameCn;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否被删除
     */
    private Boolean isDelete;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 创建时间
     */
    private Date createTime;

}
