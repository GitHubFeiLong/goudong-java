package com.zhy.authentication.server.rest.req.search;

import cn.zhxu.bs.bean.DbIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class BasePage {

    @ApiModelProperty(value = "第几页,从1开始", required = true)
    @NotNull(message = "分页查询page参数必传")
    @Min(value = 1L, message = "分页参数错误，page必须大于等于1")
    @DbIgnore
    @JsonIgnore
    private Integer page = 1;

    @ApiModelProperty(value = "一页显示内容长度", required = true)
    @NotNull(message = "分页查询size参数必传")
    @Min(value = 1L, message = "分页参数错误，size必须大于等于1")
    @DbIgnore
    @JsonIgnore
    private Integer size = 10;

    /**
     * 使用jpa 分页是从0开始
     * @return
     */
    public Integer getPage() {
        return page - 1;
    }
}
