package com.goudong.oauth2.po;

import com.goudong.commons.dto.oauth2.MetadataDTO;
import com.goudong.commons.framework.jpa.BasePO;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
@Entity
@Table(name = "base_menu")
@SQLDelete(sql = "update base_menu set deleted=true where id=?")
@Where(clause = "deleted=false")
@TypeDef(name = "json", typeClass = JsonStringType.class)
public class BaseMenuPO extends BasePO {
    private static final long serialVersionUID = -6254288573268456187L;
    /**
     * 父菜单主键
     */
    private Long parentId;
    /**
     * 前端菜单组件的信息
     * @see MetadataDTO
     */
    @Type(type = "json")
    @Column( columnDefinition = "json" )
    private MetadataDTO metadata;
    /**
     * 备注
     */
    private String remark;

    @ManyToMany(targetEntity= BaseRolePO.class, fetch = FetchType.EAGER)
    @JoinTable(name = "base_role_menu", joinColumns = {@JoinColumn(name = "menu_id")},
            inverseJoinColumns={@JoinColumn(name = "role_id")})
    private List<BaseRolePO> roles = new ArrayList<>();
}
