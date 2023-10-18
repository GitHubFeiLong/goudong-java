package com.goudong.authentication.server.easyexcel.template;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 类描述：
 * 菜单导入excel
 * @author chenf
 * @version 1.0
 */
@Data
public class BaseMenuImportExcelTemplate {
    //~fields
    //==================================================================================================================
    @ExcelProperty("* 菜单标识")
    private String permissionId;

    @ExcelProperty("上级菜单标识")
    private String parentPermissionId;

    @ExcelProperty("* 菜单名称")
    private String name;

    /**
     * 菜单,按钮,接口
     */
    @ExcelProperty("* 菜单类型")
    private String type;

    @ExcelProperty("路由或接口地址")
    private String path;

    /**
     *
     */
    @ExcelProperty("请求方式")
    private String method;

    @ExcelProperty("菜单元数据")
    private String meta;

    @ExcelProperty("菜单备注")
    private String remark;

    //~methods
    //==================================================================================================================
}
