package com.goudong.commons.dto.oauth2;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 类描述：
 * 保存白名单的参数
 * @author msi
 * @version 1.0
 * @date 2022/1/8 21:04
 */
@Data
@NoArgsConstructor
public class BaseWhitelist2CreateDTO {

    /**
     * 匹配模式
     */
    private String pattern;
    /**
     * 请求的方法
     * json数组字符串
     */
    private List<String> methods;
    /**
     * 备注
     */
    private String remark;
    /**
     * 是否是系统设置的
     */
    private Boolean isSystem = false;

    /**
     * 是否只能内部服务调用
     */
    private Boolean isInner = false;

    /**
     * 是否关闭该白名单
     */
    private Boolean isDisable = false;

    public BaseWhitelist2CreateDTO(String pattern, List<String> methods, String remark, Boolean isSystem, Boolean isInner, Boolean isDisable) {
        this.pattern = pattern;
        this.methods = methods;
        this.remark = remark;
        this.isSystem = isSystem;
        this.isInner = isInner;
        this.isDisable = isDisable;
    }
}
