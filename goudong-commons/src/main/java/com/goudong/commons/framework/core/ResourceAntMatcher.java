package com.goudong.commons.framework.core;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResourceAntMatcher implements Serializable {

    /**
     * 创建一个白名单需要的对象
     * @param pattern
     * @param methods
     * @return
     */
    public static ResourceAntMatcher createByWhitelist(String pattern, List<String> methods) {
        return new ResourceAntMatcher(pattern, methods);
    }

    /**
     * 创建一个隐藏菜单需要的对象
     * @param pattern
     * @param method
     * @return
     */
    public static ResourceAntMatcher createByHideMenu(String pattern, String method) {
        return new ResourceAntMatcher(pattern, method);
    }

    /**
     * 创建一个api接口资源需要的对象
     * @param pattern
     * @param method
     * @return
     */
    public static ResourceAntMatcher createByApiResource(String pattern, String method) {
        return new ResourceAntMatcher(pattern, method);
    }


    /**
     * 请求资源路径(使用ant方式)
     */
    private String pattern;
    /**
     * 请求方式集合
     */
    private List<String> methods;

    /**
     * 请求方式
     */
    private String method;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否只能内部服务调用
     * 由于内部服务本来就是白名单的一种特殊，暂时先不打算单独存表
     */
    private Boolean isInner = false;

    /**
     * 是否关闭
     * true关闭，false开启
     */
    private Boolean isDisable = false;

    private Boolean sys = false;

    private Boolean api = false;

    public ResourceAntMatcher(String pattern, String method) {
        this.pattern = pattern;
        this.method = method;
    }

    public ResourceAntMatcher(String pattern, List<String> methods) {
        this.pattern = pattern;
        this.methods = methods;
    }

    public ResourceAntMatcher(String pattern, List<String> methods, String remark, Boolean isInner, Boolean isDisable) {
        this.pattern = pattern;
        this.methods = methods;
        this.remark = remark;
        this.isInner = isInner;
        this.isDisable = isDisable;
    }
}
