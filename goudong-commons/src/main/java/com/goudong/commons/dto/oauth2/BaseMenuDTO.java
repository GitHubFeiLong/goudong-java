package com.goudong.commons.dto.oauth2;

import com.goudong.commons.frame.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
public class BaseMenuDTO {
    private static final long serialVersionUID = -4844654607239619613L;
    /**
     * 主键id
     */
    private Long id;
    /**
     * 父菜单主键
     */
    private Long parentId;
    /**
     * 前端菜单组件的信息
     */
    private String metadata;
    /**
     * 备注
     */
    private String remark;
}
