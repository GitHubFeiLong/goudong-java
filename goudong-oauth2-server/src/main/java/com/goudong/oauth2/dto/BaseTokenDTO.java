package com.goudong.oauth2.dto;

import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.commons.po.core.BasePO;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：
 *
 * @author msi
 * @version 1.0
 * @date 2022/1/19 20:58
 */
@Data
public class BaseTokenDTO extends BasePO {
    private static final long serialVersionUID = 2800687518810457447L;

    //~fields
    //==================================================================================================================
    /**
     * 应用Id
     */
    @NotNull
    private Long appId;
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
    private String clientType;

    /**
     * 自定义的一个会话id，用于区分发起请求的用户，可以是认证过后的token可以是未登录的cookie
     */
    private String sessionId;
    //~methods
    //==================================================================================================================

}