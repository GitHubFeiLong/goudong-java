package com.goudong.oauth2.service;


import com.goudong.commons.enumerate.oauth2.ClientSideEnum;
import com.goudong.oauth2.po.BaseUserPO;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;

/**
 * 类描述：
 * 用户服务接口
 * @author msi
 * @version 1.0
 * @date 2021/8/29 19:33
 */
public interface BaseUserService extends UserDetailsService {

    /**
     * 保存令牌和用户信息到redis中
     * @param baseUserPO 用户信息
     * @param clientSideEnum 客户端类型
     * @param accessToken 访问令牌
     */
    void saveAccessToken2Redis(BaseUserPO baseUserPO, ClientSideEnum clientSideEnum, String accessToken);

    /**
     * 获取当前请求用户认证信息
     * @param request
     * @return
     */
    BaseUserPO getAuthentication(HttpServletRequest request);

}

