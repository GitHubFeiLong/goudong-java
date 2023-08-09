package com.zhy.authentication.server.rest.req.search;

import cn.zhxu.bs.FieldOps;
import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.DbIgnore;
import cn.zhxu.bs.bean.SearchBean;
import cn.zhxu.bs.operator.Contain;
import cn.zhxu.bs.operator.StartWith;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 19:59
 */
@SearchBean(tables="base_app", orderBy = "id asc")
@Data
public class BaseAppPage extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("应用id")
    private Long id;

    @DbField(onlyOn = StartWith.class)
    @ApiModelProperty("应用名称")
    private String name;

    @ApiModelProperty("密钥")
    private String secret;

    @ApiModelProperty("激活状态")
    private Boolean enabled;

    @ApiModelProperty("备注")
    private String remark;

    //~methods
    //==================================================================================================================
}
