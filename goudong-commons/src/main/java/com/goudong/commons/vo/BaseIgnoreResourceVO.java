package com.goudong.commons.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *
 * @author
 */
@Data
@ApiModel
public class BaseIgnoreResourceVO implements Serializable {
    private static final long serialVersionUID = -6522041168978478722L;

    /**
     * 路径pattern
     */
    @ApiModelProperty("路径pattern")
    private String pattern;

    /**
     * 请求方式请求方式(多个用逗号分隔)
     */
    @ApiModelProperty("请求方式，使用英文逗号分割(GET,POST,PUT,DELETE,*)*表示所有请求方式")
    private String method;

    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
}
