package com.goudong.commons.frame.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 类描述：
 * 微服务资源接口的 pattern和httpMethod
 * @author msi
 * @date 2022/1/8 16:21
 * @version 1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceAntMatcher implements Serializable {
    /**
     * 请求资源路径(使用ant方式)
     */
    protected String pattern;
    /**
     * 请求方式
     */
    protected String method;
    /**
     * 备注
     */
    private String remark;

    public ResourceAntMatcher(String pattern, String method) {
        this.pattern = pattern;
        this.method = method;
    }
}
