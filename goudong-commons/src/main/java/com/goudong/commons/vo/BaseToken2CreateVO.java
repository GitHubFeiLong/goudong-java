package com.goudong.commons.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 类描述：
 * 新增用户token
 * @author msi
 * @version 1.0
 * @date 2021/9/5 21:56
 */
@Data
@ApiModel
@AllArgsConstructor
@Deprecated
public class BaseToken2CreateVO {

    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;
    @ApiModelProperty(value = "token字符串", required = true)
    private String token;
}
