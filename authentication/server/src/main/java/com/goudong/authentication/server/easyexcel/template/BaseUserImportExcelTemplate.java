package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 类描述：
 * 用户导入excel
 * @author chenf
 * @version 1.0
 */
@Data
public class BaseUserImportExcelTemplate {
    //~fields
    //==================================================================================================================
    @ExcelProperty("* 用户名")
    private String username;

    /**
     * 格式 yyyy-MM-dd HH:mm:ss
     */
    @ExcelProperty("* 有效期")
    private String validTime;

    /**
     * 已激活：true；未激活：false
     */
    @ExcelProperty("* 激活状态")
    private String enabled;

    /**
     * 未锁定：false；已锁定：true
     */
    @ExcelProperty("* 锁定状态")
    private String locked;

    @ExcelProperty("备注")
    private String remark;

    //~methods
    //==================================================================================================================
}
