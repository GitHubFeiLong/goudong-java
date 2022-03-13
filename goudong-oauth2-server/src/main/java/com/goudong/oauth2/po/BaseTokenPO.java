package com.goudong.oauth2.po;

import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.framework.jpa.BasePO;
import lombok.Data;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：
 * 令牌
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:21
 */
@Data
@Entity
@Table(name = "base_token")
@SQLDelete(sql = "update base_token set deleted=true where id=?")
@Where(clause = "deleted=false")
public class BaseTokenPO extends BasePO {
    private static final long serialVersionUID = -5455706615076826311L;

    //~fields
    //==================================================================================================================
    /**
     * 访问令牌
     */
    @NotBlank
    private String accessToken;
    /**
     * 刷新令牌
     */
    @NotBlank
    private String refreshToken;
    /**
     * accessToken失效时长
     */
    @NotNull
    private Date accessExpires;
    /**
     * refreshToken失效时长
     */
    @NotNull
    private Date refreshExpires;
    /**
     * 用户id
     */
    @NotNull
    private Long userId;
    /**
     * 客户端类型
     * @see ClientSideEnum#getLowerName()
     */
    @NotNull
    private String clientType;

    //~methods
    //==================================================================================================================

}