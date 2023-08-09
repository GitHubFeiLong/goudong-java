package com.zhy.authentication.server.rest.req.search;

import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.SearchBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：
 * 角色分页
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 19:59
 */
@SearchBean(
        tables="base_role br inner join base_app ba on br.app_id = ba.id",
        orderBy = "ba.id desc, bu. created_date desc"
)
@Data
public class BaseRolePage extends BasePage {
    //~fields
    //==================================================================================================================

    @NotNull
    @ApiModelProperty(value = "应用id", required = true)
    @DbField(value = "ba.id")
    private Long appId;

    @ApiModelProperty("应用名")
    @DbField("ba.name")
    private String appName;

    @ApiModelProperty("角色id")
    @DbField("br.id")
    private Long id;

    @ApiModelProperty("角色名")
    @DbField("br.name")
    private String name;

    @ApiModelProperty("备注")
    @DbField("br.remark")
    private String remark;

    @ApiModelProperty("角色创建时间")
    @DbField("br.created_date")
    private Date createdDate;
}
