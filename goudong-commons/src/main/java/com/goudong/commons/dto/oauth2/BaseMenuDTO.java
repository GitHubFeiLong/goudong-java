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
public class BaseMenuDTO implements TreeInterface {
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
    private List<BaseMenuDTO> children;

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
