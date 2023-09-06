package com.goudong.authentication.server.rest.req;

import cn.zhxu.bs.bean.SearchBean;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 应用下拉
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 19:59
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
