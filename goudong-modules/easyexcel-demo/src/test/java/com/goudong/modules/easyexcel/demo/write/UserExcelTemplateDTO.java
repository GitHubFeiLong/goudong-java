package com.goudong.modules.easyexcel.demo.write;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 *
 * @author cfl
 * @version 1.0
 * @date 2022/11/20 13:13
 */
@Data
public class UserExcelTemplateDTO implements Serializable {
    //~fields
    //==================================================================================================================

    //~methods
    //==================================================================================================================
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名", index = 0)
    @ColumnWidth(15)
    private String username;
    /**
     * 角色
     */
    @ExcelProperty(value = "角色", index = 1)
    @ColumnWidth(30)
    private String roleNameCn;
    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号", index = 2)
    @ColumnWidth(15)
    private String phone;
    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱", index = 3)
    @ColumnWidth(20)
    private String email;
    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称", index = 4)
    @ColumnWidth(15)
    private String nickname;
    /**
     * 有效期
     */
    @ExcelProperty(value = "账号有效期", index = 5)
    @ColumnWidth(20)
    private Date validTime;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ColumnWidth(20)
    private String remark;
}
