package com.goudong.user.dto;

import com.goudong.core.util.tree.v2.TreeInterface;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 菜单
 * @Author e-Feilong.Chen
 * @Date 2021/8/12 15:23
 */
@Data
public class BaseMenuDTO implements TreeInterface<Long, Long, BaseMenuDTO>, Comparable<BaseMenuDTO> {
    private static final long serialVersionUID = -4844654607239619613L;

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("父菜单主键")
    private Long parentId;

    @ApiModelProperty("菜单名")
    private String name;

    @ApiModelProperty("菜单类型（0：接口；1：菜单；2：按钮）")
    private Integer type;

    @ApiModelProperty("打开方式（0：内链；1：外链）")
    private Integer openModel;

    @ApiModelProperty("前端的路由或后端的接口")
    private String path;

    @ApiModelProperty("前端的路由指向的组件地址")
    private String componentPath;

    @ApiModelProperty("请求方式json数组 [\"GET\",\"POST\"]")
    private String method;

    @ApiModelProperty("图标")
    private String icon;

    @ApiModelProperty("权限标识（前端的菜单和按钮需要）")
    private String permissionId;

    @ApiModelProperty("排序字段（值越小越靠前，仅仅针对前端路由）")
    private Integer sortNum;

    @ApiModelProperty("是否是隐藏菜单")
    private Boolean hide;

    @ApiModelProperty("前端菜单组件的信息")
    private String metadata;

    @ApiModelProperty("子节点")
    private List<BaseMenuDTO> children;

    @ApiModelProperty("是否被选中")
    private Boolean checked;

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
    @Override
    public int compareTo(BaseMenuDTO o) {
        return this.getSortNum().compareTo(o.getSortNum());
    }
}
