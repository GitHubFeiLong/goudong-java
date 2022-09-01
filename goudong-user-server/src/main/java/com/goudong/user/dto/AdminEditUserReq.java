package com.goudong.user.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * admin 平台修改用户接口
 * @author cfl
 * @version 1.0
 * @date 2022/8/31 22:44
 */
@Data
public class AdminEditUserReq {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "用户id", required = true)
    @NotNull
    private Long id;

    @ApiModelProperty(value = "角色id，不能为空", required = true)
    @NotEmpty
    private List<Long> roleIds;

    @ApiModelProperty(value = "昵称")
    private String nickname;

    @ApiModelProperty(value = "头像地址")
    private String avatar;

    @ApiModelProperty(value = "有效期", required = true)
    @NotNull
    private Date validTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    //~methods
    //==================================================================================================================
}
