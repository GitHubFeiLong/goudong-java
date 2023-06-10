package com.goudong.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.http.HttpMethod;

import java.util.List;

/**
 * 类描述：
 * 初始化菜单参数
 * @author cfl
 * @version 1.0
 * @date 2022/9/13 21:00
 */
@Data
public class InitMenuReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "菜单名字", required = true)
    private String name;
    /**
     * @see com.goudong.user.po.BaseMenuPO.TypeEnum
     */
    @ApiModelProperty(value = "类型", required = true)
    private Integer type;

    /**
     * @see com.goudong.user.po.BaseMenuPO.OpenModelEnum
     */
    @ApiModelProperty(value = "打开方式", required = true)
    private Integer openModel;

    /**
     * 前端的路由或后端的接口，
     */
    @ApiModelProperty("前端的路由或后端的接口")
    private String path;

    @ApiModelProperty(value = "菜单唯一标识", required = true)
    private String permissionId;

    @Deprecated
    private Boolean hide;


    /**
     * 请求方式,值为{@link HttpMethod}元素的name(),例如：GET，POST。
     * 这里path 和 method 是一对一的方式，方便更细粒度鉴权。
     * 例如："[\"GET\"]"
     */
    @ApiModelProperty("请求方式， 数组JSON字符串")
    private String method;

    @ApiModelProperty("路由元数据，JSON字符串")
    private String meta;

    /**
     * 子菜单
     */
    @ApiModelProperty("子菜单")
    private List<InitMenuReq> children;
    //~methods
    //==================================================================================================================
}
