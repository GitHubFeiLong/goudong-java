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
    /**
     * 菜单名字
     */
    @ApiModelProperty("菜单名字")
    private String name;


    private Integer type;
    private Integer openModel;
    private Integer sortNum;
    private Boolean hide;

    /**
     * 前端的路由或后端的接口，
     */
    @ApiModelProperty("前端的路由或后端的接口")
    private String path;

    /**
     * 请求方式,值为{@link HttpMethod}元素的name(),例如：GET，POST。
     * 这里path 和 method 是一对一的方式，方便更细粒度鉴权。
     */
    @ApiModelProperty("请求方式")
    private String method;

    /**
     * 子菜单
     */
    @ApiModelProperty("子菜单")
    private List<InitMenuReq> children;
    //~methods
    //==================================================================================================================
}
