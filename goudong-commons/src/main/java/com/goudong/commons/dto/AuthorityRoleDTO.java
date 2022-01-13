package com.goudong.commons.dto;

import com.goudong.commons.frame.mybatisplus.BasePO;
import lombok.Data;

/**
 * 角色 DTO 传输层对象
 * authority_role
 * @author
 */
@Data
public class AuthorityRoleDTO extends BasePO {
    private static final long serialVersionUID = -2027717154119802886L;

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
