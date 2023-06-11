package com.goudong.oauth2.dto;

import com.goudong.boot.web.core.BasePage;
import com.goudong.commons.constant.core.DateConst;
import com.goudong.oauth2.po.BaseAppPO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Range;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * 类描述：
 * 应用查询对象
 * @author cfl
 * @version 1.0
 * @date 2023/6/10 22:57
 */
@Data
public class BaseAppQueryReq extends BasePage {
    //~fields
    //==================================================================================================================
    @ApiModelProperty(value = "应用名称")
    private String appName;

    /**
     * @see BaseAppPO.StatusEnum#getId()
     */
    @ApiModelProperty(value = "状态（0：待审核；1：通过；2：拒绝；）")
    @Range(min = 0, max = 2)
    private Integer status;

    @ApiModelProperty(value = "创建时间-开始时间")
    @DateTimeFormat(pattern = DateConst.DATE_TIME_FORMATTER)
    private LocalDateTime startCreateTime;

    @ApiModelProperty(value = "创建时间-结束时间")
    @DateTimeFormat(pattern = DateConst.DATE_TIME_FORMATTER)
    private LocalDateTime endCreateTime;

    //~methods
    //==================================================================================================================
}
