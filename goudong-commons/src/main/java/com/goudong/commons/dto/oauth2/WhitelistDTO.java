package com.goudong.commons.dto.oauth2;

import lombok.Data;

import java.io.Serializable;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/15 18:08
 */
@Data
public class WhitelistDTO implements Serializable {
    //~fields
    //==================================================================================================================
    private static final long serialVersionUID = 3149283918517679735L;
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

    //~methods
    //==================================================================================================================

}