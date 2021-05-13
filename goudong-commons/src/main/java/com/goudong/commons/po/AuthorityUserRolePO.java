package com.goudong.commons.po;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * authority_user_role
 * @author
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class AuthorityUserRolePO implements Serializable {
    /**
     * uuid
     */
    private String uuid;

    /**
     * 用户基本信息表uuid
     */
    private String userUuid;

    /**
     * 角色表uuid
     */
    private String roleUuid;

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
