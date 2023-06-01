package com.goudong.user.dto;

import com.goudong.commons.dto.oauth2.BaseMenuDTO;
import com.goudong.commons.po.core.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 角色
 * authority_role
 * @author
 */
@Data
public class BaseRoleDTO extends BasePO {

    /**
     * 角色名称(必须以ROLE_起始命名)
     */
    @ApiModelProperty(value = "角色名称")
    private String roleName;

    /**
     * 角色名称中文
     */
    @ApiModelProperty(value = "角色名称中文")
    private String roleNameCn;

    /**
     * 备注
     */
    @ApiModelProperty(value = "备注")
    private String remark;

    /**
     * 菜单
     */
    @ApiModelProperty(value = "角色拥有的菜单")
    private List<BaseMenuDTO> menus;

    /**
     * 权限
     */
    @ApiModelProperty(value = "权限")
    private List<BaseMenuDTO> permission;

    @ApiModelProperty(value = "角色下的用户数量")
    private Integer users;
}
