package com.goudong.commons.dto.user;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 白名单放在Redis中的属性
 * @author msi
 * @version 1.0
 * @date 2022/1/8 21:04
 */
@Data
public class BaseWhitelist2RedisDTO {

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
}
