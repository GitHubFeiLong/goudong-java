package com.goudong.commons.po;

import com.baomidou.mybatisplus.annotation.TableName;
import com.goudong.commons.framework.mybatisplus.BasePO;
import lombok.Data;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2021/9/5 22:02
 */
@Data
@TableName("base_token")
public class BaseTokenPO extends BasePO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * token字符串
     */
    private String token;

    /**
     * token md5数字加密
     */
    private String tokenMd5;
}
