package com.goudong.authentication.server.rest.req.search;

import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.SearchBean;
import cn.zhxu.bs.operator.Contain;
import cn.zhxu.bs.operator.StartWith;
import com.goudong.authentication.server.domain.BaseApp;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 应用下拉
 * @author cfl
 * @version 1.0
 */
@SearchBean(tables="base_app", orderBy = "id asc")
@Data
public class BaseAppDropDown {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("应用id")
    private Long id;

    @ApiModelProperty("应用名")
    private String name;

}
