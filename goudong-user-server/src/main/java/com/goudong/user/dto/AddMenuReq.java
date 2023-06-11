package com.goudong.user.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goudong.boot.web.core.BasicException;
import com.goudong.core.util.AssertUtil;
import com.goudong.core.util.CollectionUtil;
import com.goudong.user.po.BaseMenuPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;
import java.util.List;

import static com.goudong.commons.constant.core.HttpMethodConst.ALL_HTTP_METHOD;

/**
 * 类描述：
 * 新增菜单
 * @author cfl
 * @version 1.0
 * @date 2023/6/10 1:48
 */
@Data
public class AddMenuReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("上级菜单id（如果为空就是一级菜单）")
    private Long parentId;

    @ApiModelProperty(value = "菜单名称", required = true)
    private String name;

    @ApiModelProperty(value = "菜单类型（0：接口；1：菜单；2：按钮）", required = true)
    @NotNull
    @Range(min = 0, max = 2)
    private Integer type;

    @ApiModelProperty(value = "打开方式（0：内链；1：外链）", required = true)
    @NotNull
    @Range(min = 0, max = 1)
    private Integer openModel;

    @ApiModelProperty(value = "菜单图标", required = false)
    private String icon;

    @ApiModelProperty(value = "权限标识（前端的菜单和按钮需要）", required = false)
    private String permissionId;

    @ApiModelProperty(value = "前端的路由或后端的接口（接口菜单必传，按钮非必传传）", required = false)
    private String path;

    @ApiModelProperty(value = "请求方式(菜单不传，其余都传)", required = false)
    private String method;

    /**
     * 前端的路由指向的组件地址
     */
    @ApiModelProperty("前端的路由指向的组件地址")
    private String componentPath;

    @ApiModelProperty(value = "排序号", required = true)
    @Range(min = 1, max = Integer.MAX_VALUE)
    private Integer sortNum;

    @ApiModelProperty(value = "是否隐藏", required = true)
    private Boolean hide;

    @ApiModelProperty(value = "菜单元数据", required = false)
    private String metadata;
    //~methods
    //==================================================================================================================

    /**
     * 请求参数校验
     */
    public void check() {
        BaseMenuPO.TypeEnum typeEnum = BaseMenuPO.TypeEnum.API_INTERFACE.getById(this.getType());
        switch (typeEnum) {
            case API_INTERFACE:
                // 路由地址，请求方式 校验
                AssertUtil.isNotBlank(this.getPath(), () -> BasicException.client("接口需要填写请求路由"));
                try {
                    List list = new ObjectMapper().readValue(this.getMethod(), List.class);

                    AssertUtil.isTrue(CollectionUtil.isNotEmpty(list) && ALL_HTTP_METHOD.containsAll(list)
                            , () -> BasicException.client("接口需要填写正确的请求方式"));
                } catch (JsonProcessingException e) {
                    throw BasicException.client("请填写正确的请求方式");
                }
                break;
            case MENU:
                // 路由地址
                AssertUtil.isNotBlank(this.getPath(), () -> BasicException.client("菜单需要填写路由"));
                break;
            case BUTTON:
                // 一定要有权限标识
                AssertUtil.isNotBlank(this.getPermissionId(), () -> BasicException.client("按钮需要填写权限标识"));
                break;
        }
    }
}
