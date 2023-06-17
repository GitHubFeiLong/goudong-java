package com.goudong.oauth2.dto.authentication;

import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 类描述：
 * 登录认证成功，返回的信息
 * @Author e-Feilong.Chen
 * @Date 2022/1/20 8:06
 */
@Data
public class LoginInfoDTO {
    //~fields
    //==================================================================================================================
    /**
     * 访问令牌
     */
    @NotBlank
    @ApiModelProperty(value = "访问令牌")
    private String accessToken;
    /**
     * 刷新令牌
     */
    @NotBlank
    @ApiModelProperty(value = "刷新令牌")
    private String refreshToken;
    /**
     * accessToken失效时长
     */
    @NotNull
    @ApiModelProperty(value = "accessToken失效时长")
    private Date accessExpires;
    /**
     * refreshToken失效时长
     */
    @NotNull
    @ApiModelProperty(value = "refreshToken失效时长")
    private Date refreshExpires;

    /**
     * 客户端类型
     * @see ClientSideEnum
     */
    @ApiModelProperty(value = "客户端类型")
    private String clientType;

    /**
     * 用户信息
     */
    @ApiModelProperty(value = "用户信息")
    private BaseUserDTO user;
    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
}
