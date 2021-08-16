package com.goudong.commons.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 类描述：
 * 表的元字段，除了中间表和一些少许单表，其它的所有表都应该优的字段
 * @Author msi
 * @Date 2021-08-14 20:41
 * @Version 1.0
 */
@Data
public class BasePO implements Serializable {
    private static final long serialVersionUID = 8647446252613184267L;
    /**
     * id
     */
    protected Long id;
    /**
     * 是否被删除
     */
    protected Boolean deleted;
    /**
     * 创建数据时的操作用户id
     */
    @TableField(fill = FieldFill.INSERT)
    protected Long createUserId;
    /**
     * 更新数据时的操作用户id
     */
    @TableField(fill = FieldFill.UPDATE)
    protected Long updateUserId;
    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime updateTime;
    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    protected LocalDateTime createTime;
}
