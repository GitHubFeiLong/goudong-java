package com.goudong.file.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.goudong.commons.dto.oauth2.BaseRoleDTO;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 用户导出实体
 * @author cfl
 * @version 1.0
 * @date 2022/9/3 21:49
 */
@Data
public class BaseUserExportDTO {
    //~fields
    //==================================================================================================================
    /**
     * 序号
     */
    @ExcelProperty(value = "序号",converter= LongStringConverter.class)
    @ColumnWidth(10)
    private Long serialNumber;
    /**
     * 用户名
     */
    @ExcelProperty(value = "用户名")
    @ColumnWidth(15)
    private String username;
    /**
     * 角色
     */
    @ExcelProperty(value = "角色")
    @ColumnWidth(30)
    private String roleNameCn;
    /**
     * 手机号
     */
    @ExcelProperty(value = "手机号")
    @ColumnWidth(15)
    private String phone;
    /**
     * 邮箱
     */
    @ExcelProperty(value = "邮箱")
    @ColumnWidth(20)
    private String email;
    /**
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    @ColumnWidth(15)
    private String nickname;
    /**
     * 有效期
     */
    @ExcelProperty(value = "账号有效期")
    @ColumnWidth(20)
    private Date validTime;
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ColumnWidth(20)
    private Date createTime;
    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ColumnWidth(20)
    private String remark;

    @ExcelIgnore
    private List<BaseRoleDTO> roles;
    //~methods
    //==================================================================================================================
}
