package com.goudong.commons.entity;

import lombok.Data;

import java.util.Date;

/**
 * 无效邮箱
 * @Author msi
 * @Date 2021-05-10 9:46
 * @Version 1.0
 */
@Data
public class InvalidEmailDO {
    /**
     * 主键
     */
    private String uuid;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 是否被删除
     */
    private Boolean isDelete;
    /**
     * 修改时间
     */
    private Date updateTime;
    /**
     * 注册时间
     */
    private Date createTime;

}
