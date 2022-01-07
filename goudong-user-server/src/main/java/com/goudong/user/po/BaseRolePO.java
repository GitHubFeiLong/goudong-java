package com.goudong.user.po;

import com.goudong.commons.core.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 角色
 * authority_role
 * @author
 */
@Data
@Entity
@Table(name = "base_role")
@SQLDelete(sql = "update base_role set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseRolePO extends BasePO {

    private static final long serialVersionUID = 3961964136793768410L;
    /**
     * 角色名称(必须以ROLE_起始命名)
     */
    @Column(name = "role_name")
    private String roleName;

    /**
     * 角色名称中文
     */
    @Column(name = "role_name_cn")
    private String roleNameCn;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}
