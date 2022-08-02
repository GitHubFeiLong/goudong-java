package com.goudong.commons.dto.oauth2;

import com.goudong.commons.po.core.BasePO;
import lombok.Data;

import java.io.Serializable;


/**
 * 类描述：
 * 保存api接口资源(BaseApiResource)的参数
 * @author cfl
 * @date 2022/8/2 22:50
 * @version 1.0
 */
@Data
public class BaseApiResourceDTO extends BasePO implements Serializable {
    /**
     * 路径Pattern
     */
    private String pattern;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 接口所在应用
     */
    private String applicationName;
    /**
     * 备注
     */
    private String remark;
}

