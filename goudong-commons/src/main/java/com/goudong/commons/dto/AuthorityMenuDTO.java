package com.goudong.commons.dto;

import com.goudong.commons.frame.mybatisplus.BasePO;
import lombok.Data;

/**
 * 类描述：
 * 菜单表
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
public class AuthorityMenuDTO extends BasePO {
    private static final long serialVersionUID = 7234374806123502046L;
    /**
     * 请求url
     */
    private String url;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 菜单名字
     */
    private String menuName;
    /**
     * 父菜单主键
     */
    private Long parentId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 应用名称
     */
    private String applicationName;
    /**
     * 类型 0:系统接口；1：前端页面
     * 默认0
     */
    private Integer type;
}
