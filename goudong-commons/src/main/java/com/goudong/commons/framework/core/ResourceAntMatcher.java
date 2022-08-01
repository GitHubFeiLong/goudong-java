package com.goudong.commons.framework.core;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 类描述：
 * 微服务资源接口的 pattern和httpMethod
 * @author msi
 * @date 2022/1/8 16:21
 * @version 1.0
 */
@Data
public class ResourceAntMatcher implements Serializable {
    /**
     * 请求资源路径(使用ant方式)
     */
    protected String pattern;
    /**
     * 请求方式
     */
    protected List<String> methods;
    /**
     * 备注
     */
    private String remark;

    /**
     * 是否只能内部服务调用
     */
    private Boolean isInner = false;

    /**
     * 是否关闭
     * true关闭，false开启
     */
    private Boolean disable = false;

    public ResourceAntMatcher(String pattern, List<String> methods, String remark) {
        this.pattern = pattern;
        this.methods = methods;
        this.remark = remark;
    }

    public ResourceAntMatcher(String pattern, List<String> methods, String remark, Boolean isInner) {
        this.pattern = pattern;
        this.methods = methods;
        this.remark = remark;
        this.isInner = isInner;
    }

    public ResourceAntMatcher(String pattern, List<String> methods, String remark, Boolean isInner, Boolean disable) {
        this.pattern = pattern;
        this.methods = methods;
        this.remark = remark;
        this.isInner = isInner;
        this.disable = disable;
    }
}
