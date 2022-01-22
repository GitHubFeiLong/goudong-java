package com.goudong.oauth2.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.goudong.oauth2.core.AuthenticationImpl;
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
     * 获取当前请求用户认证信息
     * @param request
     * @return
     */
    AuthenticationImpl getAuthentication(HttpServletRequest request) throws JsonProcessingException;

}

