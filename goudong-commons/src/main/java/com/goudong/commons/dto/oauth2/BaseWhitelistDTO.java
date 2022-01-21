package com.goudong.commons.dto.oauth2;

import com.goudong.commons.po.core.BasePO;
import lombok.Data;

import java.util.Date;

/**
 * 类描述：
 * 白名单DTO
 * @author msi
 * @version 1.0
 * @date 2022/1/8 21:04
 */
@Data
public class BaseWhitelistDTO extends BasePO {

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
    /**
     * 是否是系统设置的
     */
    private Boolean isSystem;
}
