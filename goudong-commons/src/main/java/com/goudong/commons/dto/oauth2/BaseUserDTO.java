package com.goudong.commons.dto.oauth2;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 类描述：
 * 用户表
 * @author msi
 * @date 2021/12/19 14:20
 * @version 1.0
 */
@Data
public class BaseUserDTO {

    private static final long serialVersionUID = -1209701285445397589L;

    /**
     * 主键
     */
    private Long id;
    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 备注
     */
    private String remark;

    /**
     * 有效截止时间
     */
    private Date validTime;

    /**
     * qq登录后，系统获取腾讯的open_id
     */
    private String qqOpenId;

    /**
     * 角色
     */
    private List<BaseRoleDTO> roles;

    /**
     * 菜单
     */
    private List<BaseMenuDTO> menus;


    /**
     * 自定义的一个会话id，用于区分发起请求的用户，可以是认证过后的token可以是未登录的cookie
     */
    private String sessionId;
}
