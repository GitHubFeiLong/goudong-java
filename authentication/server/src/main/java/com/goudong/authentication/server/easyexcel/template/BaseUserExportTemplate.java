package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 类描述：
 * 用户导出
 * @author chenf
 * @version 1.0
 */
@Data
public class BaseUserExportTemplate {
    //~fields
    //==================================================================================================================
    @ApiModelProperty("序号")
    @ExcelProperty("序号")
    private Integer sequenceNumber;

    @ApiModelProperty("用户名")
    @ExcelProperty("用户名")
    @ColumnWidth(25)
    private String username;

    @ApiModelProperty("角色列表")
    @ExcelProperty("角色列表")
    @ColumnWidth(25)
    private String roles;

    @ApiModelProperty("用户过期时间")
    private Date validTime;

    @ApiModelProperty("激活状态")
    private String enabled;

    @ApiModelProperty("锁定状态")
    private String locked;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建时间")
    private Date createdDate;

    //~methods
    //==================================================================================================================
}
