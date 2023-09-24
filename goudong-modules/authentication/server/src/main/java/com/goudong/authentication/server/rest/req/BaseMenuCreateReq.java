package com.goudong.authentication.server.rest.req;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 新增菜单
 */
@Data
public class BaseMenuCreateReq implements Serializable {

    @ApiModelProperty("上级菜单id")
    private Long parentId;

    @ApiModelProperty(value = "类型（1：菜单；2：按钮；3：接口）", required = true)
    @Range(min = 1, max = 3, message = "菜单类型参数应该处于1~3之间")
    @NotNull
    private Integer type;

    @ApiModelProperty(value = "菜单名", required = true)
    @NotBlank
    private String name;

    @ApiModelProperty("权限标识，类型菜单按钮必填")
    private String permissionId;

    @ApiModelProperty("路由地址")
    private String path;

    @ApiModelProperty("请求方式（json数组，例如['GET','POST',...]）")
    private String method;

    @ApiModelProperty("排序号")
    @Min(value = 1, message = "排序号取值范围1~2147483647")
    private Integer sortNum;

    @ApiModelProperty("是否隐藏")
    private Boolean hide;

    @ApiModelProperty("菜单元数据（json格式）")
    private String meta;

    @ApiModelProperty("备注")
    private String remark;
}
