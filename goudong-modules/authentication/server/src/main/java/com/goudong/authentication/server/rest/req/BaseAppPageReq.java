package com.goudong.authentication.server.rest.req;

import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.SearchBean;
import cn.zhxu.bs.operator.Contain;
import cn.zhxu.bs.operator.StartWith;
import com.goudong.authentication.server.rest.req.search.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * BeanSearch查询参数和返回值
 * @author cfl
 */
@SearchBean(tables="base_app", orderBy = "id asc")
@Data
public class BaseAppPageReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("应用id")
    private Long id;

    @DbField(onlyOn = StartWith.class)
    @ApiModelProperty("应用名称")
    private String name;

    @ApiModelProperty("密钥")
    private String secret;

    @ApiModelProperty("首页地址")
    @DbField(onlyOn = Contain.class)
    private String homePage;

    @ApiModelProperty("激活状态")
    private Boolean enabled;

    @ApiModelProperty("创建时间")
    @DbField(conditional = false)
    private Date createdDate;

    @ApiModelProperty("备注")
    @DbField(onlyOn = Contain.class)
    private String remark;

    //~methods
    //==================================================================================================================
}
