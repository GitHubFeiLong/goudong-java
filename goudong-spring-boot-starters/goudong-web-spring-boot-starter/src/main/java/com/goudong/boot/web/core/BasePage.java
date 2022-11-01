package com.goudong.boot.web.core;

import com.goudong.boot.web.util.PageTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;
import lombok.ToString;

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
@ToString
public class BasePage {

    @NotNull(message = "分页查询page参数必传")
    @Min(value = 1, message = "分页参数错误，page必须大于等于1")
    @ApiModelProperty(value = "第几页,从1开始",required = true)
    private Integer page;

    @NotNull(message = "分页查询size参数必传")
    @Min(value = 1, message = "分页参数错误，size必须大于等于1")
    @ApiModelProperty(value = "一页显示内容长度", required = true)
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public Integer getSize() {
        return size;
    }

    /**
     * 获取spring-data-jpa 框架的页码（以0开始），
     * @return
     */
    @ApiModelProperty(hidden = true)
    public Integer getJPAPage() {
        return page == null ? null : PageTypeEnum.JPA.getPage(page);
    }


    /**
     * 获取mybatis-plus 框架的页码
     * @return
     */
    @ApiModelProperty(hidden = true)
    public Integer getMPPage() {
        return page == null ? null : PageTypeEnum.MYBATIS_PLUS.getPage(page);
    }
}
