package com.goudong.oauth2.dto;

import com.goudong.commons.po.core.BasePO;
import lombok.Data;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/2/4 11:49
 */
@Data
public class BaseAuthenticationLogDTO extends BasePO {

    //~fields
    //==================================================================================================================
    /**
     * 认证的主要信息（用户名，电话，邮箱）
     */
    private String principal;

    /**
     * ip地址,使用整数存储，当获取不到ip时使用0
     */
    private Long ip;

    /**
     * 是否是认证成功
     */
    private Boolean successful;

    /**
     * 认证类型（system:系统用户,qq:qq快捷登录，wei_xin：微信快捷登录）
     */
    private String type;

    /**
     * 失败描述
     */
    private String description;
    //~methods
    //==================================================================================================================


    public BaseAuthenticationLogDTO() {
    }

    public BaseAuthenticationLogDTO(String principal, Boolean successful, String type, String description) {
        this.principal = principal;
        this.successful = successful;
        this.type = type;
        this.description = description;
    }
}