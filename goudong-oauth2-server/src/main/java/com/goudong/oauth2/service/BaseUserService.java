package com.goudong.oauth2.service;


import com.goudong.oauth2.dto.authentication.BaseUserDTO;
import com.goudong.oauth2.po.BaseUserPO;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
     * @param accessToken 访问令牌
     */
    void saveAccessToken2Redis(BaseUserPO baseUserPO, String accessToken);

    /**
     * 获取当前请求用户认证信息
     * @param request
     * @return
     */
    BaseUserPO getAuthentication(HttpServletRequest request);

    /**
     * 根据用户id查询用户信息
     * @param userId
     * @return
     */
    BaseUserDTO findById(Long userId);

    /**
     * 根据openId查询用户信息
     * @param openId
     * @return
     */
    BaseUserDTO findByOpenId(String openId);

    /**
     * 应用审核通过后，进行创建admin用户信息
     * @param id
     */
    @Transactional(propagation = Propagation.REQUIRED)
    void saveAppAdminUser(Long id);
}

