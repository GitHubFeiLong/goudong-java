package com.goudong.commons.po.core;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 类描述：
 * 表的元字段，除了中间表和一些少许单表，其它的所有表都应该优的字段
 * @Author msi
 * @Date 2021-08-14 20:41
 * @Version 1.0
 */
@Data
public class BasePO implements Serializable {
    /**
     * id
     * 防止前端接受精度丢失
     */
    protected Long id;
    /**
     * appId
     */
    protected Long appId;
    /**
     * 是否被删除
     */
    protected Boolean deleted;
    /**
     * 创建数据时的操作用户id
     */
    protected Long createUserId;
    /**
     * 更新数据时的操作用户id
     */
    protected Long updateUserId;
    /**
     * 更新时间
     */
    protected Date updateTime;
    /**
     * 创建时间
     */
    protected Date createTime;
}
