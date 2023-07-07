package com.goudong.file.dto;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

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
     * 昵称
     */
    @ExcelProperty(value = "昵称")
    @ColumnWidth(15)
    private String nickname;

    /**
     * 昵称
     */
    @ExcelProperty(value = "性别")
    @ColumnWidth(10)
    private String sex;

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
     * 角色
     */
    @ExcelProperty(value = "角色")
    @ColumnWidth(30)
    private String roleNameCn;

    /**
     * 备注
     */
    @ExcelProperty(value = "备注")
    @ColumnWidth(20)
    private String remark;

    /**
     * 有效期
     */
    @ExcelProperty(value = "账号有效期")
    @ColumnWidth(20)
    private LocalDateTime validTime;
    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    @ColumnWidth(20)
    private Date createTime;

    /**
     * 激活状态（true：激活；false：未激活）
     */
    @ExcelProperty(value = "激活状态")
    private String enabled;

    /**
     * 激活状态（true：锁定；false：未锁定）
     */
    @ExcelProperty(value = "锁定状态")
    private String locked;

    //~methods
    //==================================================================================================================
}
