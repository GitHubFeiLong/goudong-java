package com.goudong.user.po;

import com.goudong.commons.framework.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 * authority_role
 * @author
 */
@Data
@Entity
@Table(name = "base_role")
@SQLDelete(sql = "update base_role set deleted=null where id=?")
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

    @ManyToMany(targetEntity=BaseUserPO.class)
    @JoinTable(name = "base_user_role", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns={@JoinColumn(name = "user_id")})
    private List<BaseUserPO> users = new ArrayList<>();
}
