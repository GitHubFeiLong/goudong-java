package com.goudong.oauth2.core;

import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 登录认证成功，返回的信息
 * @Author e-Feilong.Chen
 * @Date 2022/1/20 8:06
 */
@Data
public class LoginInfo {
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
     * @see ClientSideEnum
     */
    private String clientType;

    /**
     * 用户信息
     */
    private BaseUser user;

    /**
     * 角色
     */
    private List<BaseRole> authorities;

    /**
     * 菜单
     */
    private List<BaseMenu> menus;

    //~construct methods
    //==================================================================================================================

    //~methods
    //==================================================================================================================
}
