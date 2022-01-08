package com.goudong.commons.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 类描述：
 * 微服务资源接口的 pattern和httpMethod
 * @Author e-Feilong.Chen
 * @Date 2021/8/16 10:54
 */
@Deprecated
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResourceAntMatcher implements Serializable {
    private static final long serialVersionUID = -4373921747237641441L;
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
