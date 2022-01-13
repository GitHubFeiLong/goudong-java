package com.goudong.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.goudong.commons.frame.mybatisplus.BasePO;
import lombok.Data;

/**
 * authority_ignore_resource
 * @author
 */
@Data
@TableName("base_ignore_resource")
public class BaseIgnoreResourcePO extends BasePO {
    /**
     * 路径pattern
     */
    private String pattern;

    /**
     * 请求方式请求方式(多个用逗号分隔)
     */
    private String method;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否是系统预置数据
     */
    private Boolean isSystem;
}
