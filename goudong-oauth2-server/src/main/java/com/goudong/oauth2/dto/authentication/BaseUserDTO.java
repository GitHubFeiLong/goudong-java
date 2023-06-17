package com.goudong.oauth2.dto.authentication;

import com.goudong.commons.po.core.BasePO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 类描述：
 * 用户
 * @author msi
 * @date 2021/12/19 14:20
 * @version 1.0
 */
@Data
public class BaseUserDTO extends BasePO {

    private static final long serialVersionUID = -1209701285445397589L;

    @ApiModelProperty("用户id")
    private Long id;

    @ApiModelProperty("应用Id")
    private Long appId;

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("昵称")
    private String nickname;

    @ApiModelProperty("角色")
    private List<BaseRoleDTO> roles;

    @ApiModelProperty("菜单")
    private List<BaseMenuDTO> menus;

    @ApiModelProperty("头像地址")
    private String avatar;

    @ApiModelProperty("激活状态（true：激活；false：未激活）")
    private Boolean enabled;

    @ApiModelProperty("性别（0：未知；1：男；2：女）")
    private Integer sex;

    @ApiModelProperty("锁定状态（true：已锁定；false：未锁定）")
    private Boolean locked;
}
