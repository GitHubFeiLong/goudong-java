package com.goudong.commons.enumerate;

import lombok.Getter;

/**
 * 类描述：
 * PO的基础字段枚举 用于mybatis-plus的字段填充
 * @Author msi
 * @Date 2021-08-14 14:21
 * @Version 1.0
 */
@Getter
public enum BasicPoFieldEnum {
    /**
     * 创建时间
     */
    CREATE_TIME("createTime"),
    /**
     * 创建数据的用户id
     */
    CREATE_USER_ID("createUserId"),
    /**
     * 更新时间
     */
    UPDATE_TIME("updateTime"),
    /**
     * 更新数据的用户id
     */
    UPDATE_USER_ID("updateUserId"),
    ;
    /**
     *
     */
    private String field;

    BasicPoFieldEnum(String field) {
        this.field = field;
    }
}
