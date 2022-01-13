package com.goudong.commons.vo;

import com.goudong.commons.frame.mybatisplus.BasePO;
import lombok.Data;

/**
 * 角色
 * authority_role
 * @author
 */
@Data
public class AuthorityRoleVO extends BasePO {

    private static final long serialVersionUID = -47431285431777174L;
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
}
