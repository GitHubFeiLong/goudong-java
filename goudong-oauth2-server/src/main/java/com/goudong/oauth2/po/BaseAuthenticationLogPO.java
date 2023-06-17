package com.goudong.oauth2.po;

import com.goudong.commons.framework.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/2/4 11:49
 */
@Data
@Entity
@Table(name = "base_authentication_log")
@SQLDelete(sql = "update base_authentication_log set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseAuthenticationLogPO extends BasePO {

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
     * ip地址,当获取不到ip时使用0
     */
    private String ipv4;

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

}