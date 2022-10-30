package com.goudong.user.dto;

import com.goudong.boot.web.core.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * 分页查询角色的接口参数实体
 * @author cfl
 * @version 1.0
 * @date 2022/8/23 20:58
 */
@Data
public class BaseRole2QueryPageDTO extends BasePage {
    //~fields
    //==================================================================================================================
    /**
     * 角色名称中文
     */
    @ApiModelProperty("角色名称中文")
    private String roleNameCn;

    //~methods
    //==================================================================================================================
}
