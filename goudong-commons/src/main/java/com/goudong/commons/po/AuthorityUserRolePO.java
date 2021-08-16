package com.goudong.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * authority_user_role
 * @author
 */
@Data
@TableName("authority_user_role")
public class AuthorityUserRolePO {
    /**
     * id
     */
    private Long id;

    /**
     * 用户基本信息表uuid
     */
    private Long userId;

    /**
     * 角色表uuid
     */
    private Long roleId;
}
