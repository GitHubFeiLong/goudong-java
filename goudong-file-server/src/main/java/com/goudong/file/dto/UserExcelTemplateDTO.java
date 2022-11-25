package com.goudong.file.dto;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.goudong.file.annotation.excel.ExcelComment;
import com.goudong.file.annotation.excel.ExcelDataValidation;
import com.goudong.file.annotation.excel.ExcelRequired;
import com.goudong.file.service.impl.BaseRoleServiceImpl;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 * index 不能重复
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
    @ExcelProperty(value = "用户名")
    @ExcelRequired
    @ColumnWidth(15)
    private String username;

    /**
     * 密码
     */
    @ExcelProperty(value = "密码")
    @ExcelComment("登录密码，如果不填写，默认值是123456")
    @ColumnWidth(15)
    private String password;

    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    @ColumnWidth(15)
    private String nickname;
    /**
     * 性别
     */
    @ExcelProperty(value = "性别")
    @ExcelDataValidation(value = {"男", "女"})
    @ColumnWidth(10)
    private String sex;
    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    @ColumnWidth(15)
    @ExcelRequired
    private String phone;
    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    @ColumnWidth(20)
    @ExcelRequired
    private String email;

    /**
     * 角色
     */
    @ExcelProperty(value = "角色")
    @ExcelComment("设置用户角色，不同角色对应着不同的权限")
    @ExcelDataValidation(source = BaseRoleServiceImpl.class)
    @ColumnWidth(30)
    @ExcelRequired
    private String roleNameCn;

    /**
     * 有效期
     */
    @ExcelProperty(value = "账号有效期")
    @ExcelComment("设置账户的有效期，如果不填写就默认永久有效")
    @ColumnWidth(20)
    private Date validTime;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ColumnWidth(20)
    private String remark;
}
