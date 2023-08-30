package com.goudong.authentication.server.service.dto;

import lombok.Data;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * 类描述：
 * 登录返回信息
 * @author cfl
 * @version 1.0
 * @date 2023/7/18 13:44
 */
@Data
@Deprecated
public class LoginDTO {

    private Long id;

    private Long appId;

    private String username;

    private String token;

    private Collection<String> roles;

    /**
     * 应用的首页地址
     */
    private String homePage;
}


