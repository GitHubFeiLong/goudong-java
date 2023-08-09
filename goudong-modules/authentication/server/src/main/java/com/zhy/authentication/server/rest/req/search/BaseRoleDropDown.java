package com.zhy.authentication.server.rest.req.search;

import cn.zhxu.bs.bean.SearchBean;
import lombok.Data;

/**
 * 类描述：
 * 角色下拉分页
 * @author cfl
 * @version 1.0
 * @date 2023/7/22 19:59
 */
@SearchBean(
        tables="base_role"
)
@Data
public class BaseRoleDropDown {
    //~fields
    //==================================================================================================================

    private Long id;

    private String name;

    private Long appId;
}
