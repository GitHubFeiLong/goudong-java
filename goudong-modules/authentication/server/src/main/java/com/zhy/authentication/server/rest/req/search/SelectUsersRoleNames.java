package com.zhy.authentication.server.rest.req.search;

import cn.zhxu.bs.bean.DbField;
import cn.zhxu.bs.bean.DbIgnore;
import cn.zhxu.bs.bean.SearchBean;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 查询用户角色名
 * @author cfl
 * @version 1.0
 * @date 2023/7/24 15:54
 */
@SearchBean(
        tables="base_user_role bur inner join base_role br on bur.role_id = br.id"
)
@Data
public class SelectUsersRoleNames {

    @ApiModelProperty("角色id")
    @DbField("br.id")
    private Long id;

    @ApiModelProperty("角色名称")
    @DbField("br.name")
    private String name;

    @ApiModelProperty(hidden = true)
    @JsonIgnore
    @DbField("bur.user_id")
    private Long userId;
}
