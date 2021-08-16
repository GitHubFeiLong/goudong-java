package com.goudong.commons.dto;

import com.goudong.commons.po.BasePO;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * authority_ignore_resource
 * @author
 */
@Data
@NoArgsConstructor
public class BaseIgnoreResourceDTO extends BasePO {
    private static final long serialVersionUID = -4109344800421282263L;
    /**
     * 路径 pattern
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

    public BaseIgnoreResourceDTO (String pattern, String method) {
        this.pattern = pattern;
        this.method = method;
    }

    public BaseIgnoreResourceDTO (String pattern, String method, String remark) {
        this.pattern = pattern;
        this.method = method;
        this.remark = remark;
    }
}
