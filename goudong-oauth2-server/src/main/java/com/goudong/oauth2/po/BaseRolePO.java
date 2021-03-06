package com.goudong.oauth2.po;

import com.goudong.commons.frame.jpa.BasePO;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 角色
 * authority_role
 * @author
 */
@Getter
@Setter
@Entity
@Table(name = "base_role")
@SQLDelete(sql = "update base_role set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseRolePO extends BasePO implements GrantedAuthority {

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

    @ManyToMany(targetEntity=BaseUserPO.class, fetch = FetchType.EAGER)
    @JoinTable(name = "base_user_role", joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns={@JoinColumn(name = "user_id")})
    private List<BaseUserPO> users = new ArrayList<>();

    @Override
    public String getAuthority() {
        return this.roleName;
    }
}
