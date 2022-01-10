package com.goudong.commons.dto.user;

import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 白名单DTO
 * @author msi
 * @version 1.0
 * @date 2022/1/8 21:04
 */
@Data
public class BaseWhitelistDTO {

    protected Long id;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建人id
     */
    private Long createUserId;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 更新人id
     */
    private Long updateUserId;

    /**
     * 删除状态 0 正常1 删除
     */
    private Boolean deleted;

    /**
     * 匹配模式
     */
    private String pattern;
    /**
     * 请求的方法
     * json数组字符串
     */
    private String methods;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否是系统设置的
     */
    private Boolean isSystem;
}
