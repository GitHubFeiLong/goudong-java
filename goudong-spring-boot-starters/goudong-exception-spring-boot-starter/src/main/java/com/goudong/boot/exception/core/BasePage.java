package com.goudong.boot.exception.core;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 类描述：
 * 分页对象
 * @Author msi
 * @Date 2021-08-19 21:37
 * @Version 1.0
 */
@Setter
@ApiModel
public class BasePage {

    @NotNull(message = "分页查询page参数必传")
    @Min(value = 1, message = "分页参数错误，page必须大于等于1")
    @ApiModelProperty(value = "第几页,从1开始",required = true)
    private long page;

    @NotNull(message = "分页查询size参数必传")
    @Min(value = 1, message = "分页参数错误，size必须大于等于1")
    @ApiModelProperty(value = "一页显示内容长度", required = true)
    private long size;

    /**
     * 获取spring-data-jpa 框架的页码（以0开始），
     * @return
     */
    @ApiModelProperty(hidden = true)
    public int getJPAPage() {
        return (int)(page - 1);
    }

    /**
     * 获取spring-data-jpa 框架的页码（以0开始），
     * @return
     */
    @ApiModelProperty(hidden = true)
    public int getJPASize() {
        return (int)(size);
    }

    /**
     * 获取mybatis-plus 框架的页码
     * @return
     */
    @ApiModelProperty(hidden = true)
    private long getMPPage() {
        return page;
    }

    /**
     * 获取mybatis-plus 框架的size
     * @return
     */
    @ApiModelProperty(hidden = true)
    private long getMPSize() {
        return size;
    }
}
