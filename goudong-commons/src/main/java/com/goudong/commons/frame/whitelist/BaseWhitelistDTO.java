package com.goudong.commons.frame.whitelist;

import lombok.Data;

/**
 * 类描述：
 * 白名单
 * @author msi
 * @version 1.0
 * @date 2022/1/8 16:46
 */
@Data
public class BaseWhitelistDTO {

    /**
     * 匹配模式使用ant模式,example: /api/user/*.
     */
    private String pattern;
    /**
     * 请求的方法,多个方法使用`,`分隔,例如：GET,POST.
     * @see org.springframework.http.HttpMethod
     */
    private String methods;
    /**
     * 备注,描述
     */
    private String remark;
    /**
     * 是否是系统设置的
     */
    private Boolean isSystem = true;
}
