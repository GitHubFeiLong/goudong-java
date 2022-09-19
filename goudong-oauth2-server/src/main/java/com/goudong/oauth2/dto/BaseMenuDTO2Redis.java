package com.goudong.oauth2.dto;

import com.goudong.commons.dto.oauth2.MetadataDTO;
import com.goudong.commons.framework.jpa.BasePO;
import lombok.Data;
import org.springframework.http.HttpMethod;

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
     * 菜单名
     */
    private String name;

    /**
     * 是否是api
     */
    private Boolean api = false;

    /**
     * 前端的路由或后端的接口，
     */
    private String path;

    /**
     * 请求方式,值为{@link HttpMethod}元素的name(),例如：GET，POST。
     * 这里path 和 method 是一对一的方式，方便更细粒度鉴权。
     */
    private String method;

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
     * 是否是系统菜单（true：是；false：不是）
     */
    private Boolean sys;

    /**
     * 是否是隐藏菜单
     */
    private Boolean hide;

    /**
     * 子节点
     */
    private List<BaseMenuDTO2Redis> children;

}
