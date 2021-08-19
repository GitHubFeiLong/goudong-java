package com.goudong.commons.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 分页对象
 * @Author msi
 * @Date 2021-08-19 21:37
 * @Version 1.0
 */
@Data
@ApiModel
public class PageParam<T> {

    @NotNull(message = "分页查询current参数必传")
    @Min(value = 1, message = "分页参数错误，current必须大于等于1")
    @ApiModelProperty(value = "当前页码", required = true)
    private int current;

    @NotNull(message = "分页查询size参数必传")
    @Min(value = 0, message = "分页参数错误，size必须大于等于0")
    @ApiModelProperty(value = "一页显示内容长度", required = true)
    private int size;

    @ApiModelProperty(value = "过滤条件")
    T data;
}
