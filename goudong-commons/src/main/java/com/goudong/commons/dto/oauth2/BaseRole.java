package com.goudong.commons.dto.oauth2;

import com.goudong.commons.frame.jpa.BasePO;
import lombok.Data;

/**
 * 角色
 * authority_role
 * @author
 */
@Data
public class BaseRole{

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
