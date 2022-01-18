package com.goudong.oauth2.core;

import com.goudong.commons.frame.jpa.BasePO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Column;

/**
 * 角色
 * authority_role
 * @author
 */
@Getter
@Setter
public class BaseRole extends BasePO implements GrantedAuthority {

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

    @Override
    public String getAuthority() {
        return this.roleName;
    }
}
