package com.goudong.commons.dto;

import com.goudong.commons.frame.mybatisplus.BasePO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 类描述：
 * base_token
 * @author msi
 * @version 1.0
 * @date 2021/9/5 21:56
 */
@Data
@ApiModel
@Deprecated
public class BaseTokenDTO extends BasePO {

    @ApiModelProperty(value = "用户id", required = true)
    private Long userId;
    @ApiModelProperty(value = "token字符串", required = true)
    private String token;
}
