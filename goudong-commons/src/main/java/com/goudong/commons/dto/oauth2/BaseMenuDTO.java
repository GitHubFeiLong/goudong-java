package com.goudong.commons.dto.oauth2;

import com.goudong.core.util.tree.v2.TreeInterface;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 类描述：
 * 菜单表,放这里是为了方便UserDTO
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
public class BaseMenuDTO implements TreeInterface<Long, Long, BaseMenuDTO> {
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
     * 菜单名
     */
    private String name;
    /**
     * 菜单类型（0：接口；1：菜单；2：按钮）
     */
    private Integer type;

    /**
     * 打开方式（0：内链；1：外链）
     */
    private Integer openModel;

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
     * 图标
     */
    private String icon;

    /**
     * 权限标识（前端的菜单和按钮需要）
     */
    private String permissionId;

    /**
     * 排序字段（值越小越靠前，仅仅针对前端路由）
     */
    private Integer sortNum;

    /**
     * 是否是隐藏菜单
     */
    private Boolean hide;

    /**
     * 前端菜单组件的信息
     */
    private MetadataDTO metadata;
    /**
     * 备注
     */
    private String remark;

    /**
     * 是否选择
     */
    private boolean checked;

    /**
     * 子节点
     */
    private List<BaseMenuDTO> children;

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
