package com.goudong.oauth2.dto;

import com.goudong.commons.dto.oauth2.MetadataDTO;
import com.goudong.commons.framework.jpa.BasePO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
public class BaseMenuDTO2Redis extends BasePO implements Serializable {

    private static final long serialVersionUID = 2514352558032373968L;
    /**
     * 父菜单主键
     */
    private Long parentId;
    /**
     * 前端菜单组件的信息
     * @see MetadataDTO
     */
    private MetadataDTO metadata;
    /**
     * 备注
     */
    private String remark;

    /**
     * 子节点
     */
    private List<BaseMenuDTO2Redis> children;

}
