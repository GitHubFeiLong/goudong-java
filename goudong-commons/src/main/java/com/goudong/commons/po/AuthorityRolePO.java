package com.goudong.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 角色
 * authority_role
 * @author
 */
@Data
@TableName("authority_role")
public class AuthorityRolePO extends BasePO {

    private static final long serialVersionUID = 3961964136793768410L;
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
