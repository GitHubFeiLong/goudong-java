package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 类描述：
 * 角色导入excel
 * @author chenf
 * @version 1.0
 */
@Data
public class BaseRoleImportExcelTemplate {
    //~fields
    //==================================================================================================================
    @ExcelProperty("* 角色名称")
    private String name;


    @ExcelProperty("角色备注")
    private String remark;

    //~methods
    //==================================================================================================================
}
