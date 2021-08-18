package com.goudong.commons.utils;

import cn.hutool.core.bean.BeanUtil;
import com.goudong.commons.dto.AuthorityMenuDTO;
import com.goudong.commons.dto.AuthorityRoleDTO;
import com.goudong.commons.dto.AuthorityUserDTO;
import com.goudong.commons.enumerate.RedisKeyEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 类描述：
 * 登录用户相关的操作，获取信息，角色，权限等。登录退出更新redis信息。
 * @Author e-Feilong.Chen
 * @Date 2021/8/17 16:22
 */
@Component
public class AuthorityUserUtil {

    private HttpServletRequest request;

    @Autowired
    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }

    private RedisOperationsUtil redisOperationsUtil;

    @Autowired
    public void setRedisOperationsUtil(RedisOperationsUtil redisOperationsUtil) {
        this.redisOperationsUtil = redisOperationsUtil;
    }

    /**
     * 登录统一处理redis
     * @param token
     * @param authorityUserDTO
     */
    public void login (String token, AuthorityUserDTO authorityUserDTO) {
        redisOperationsUtil.login(token, authorityUserDTO);
    }

    /**
     * 退出统一处理redis
     * @param token
     */
    public void logout(String token, Long userId) {
        redisOperationsUtil.logout(token, userId);
    }

    /**
     * 使用HttpServletRequest 获取用户信息
     * 原理就是使用请求头的Authorization值，使用md5生成一个16位的key，从redis获取对象
     * @return
     */
    public AuthorityUserDTO getUserDetails(){
        // 获取token
        String token = this.request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        return getUserDetails(token);

    }
    /**
     * 根据请求对象获取用户id
     * @return
     */
    public Long getUserId() {
        return getUserDetails().getId();
    }

    /**
     * 根据请求对象，获取用户角色
     * @return
     */
    public List<AuthorityRoleDTO> getUserRoleDetails () {
        return getUserDetails().getAuthorityRoleDTOS();
    }

    /**
     * 根据请求对象，获取用户权限
     * @return
     */
    public List<AuthorityMenuDTO> getUserMenuDetails () {
        return getUserDetails().getAuthorityMenuDTOS();
    }


    /**
     * 使用Token 获取用户信息
     * @param token
     * @return
     */
    public AuthorityUserDTO getUserDetails(String token){
        // md5加密的key
        String key = JwtTokenUtil.generateRedisKey(token);

        AuthorityUserDTO userDTO = redisOperationsUtil.getHashValue(RedisKeyEnum.OAUTH2_USER_INFO, AuthorityUserDTO.class, key);
        return userDTO;
    }

    /**
     * 根据token获取用户id
     * @param token
     * @return
     */
    public Long getUserId(String token) {
        return getUserDetails(token).getId();
    }

    /**
     * 根据token，获取用户角色
     * @param token
     * @return
     */
    public List<AuthorityRoleDTO> getUserRoleDetails (String token) {
        return getUserDetails(token).getAuthorityRoleDTOS();
    }

    /**
     * 根据token，获取用户权限
     * @param token
     * @return
     */
    public List<AuthorityMenuDTO> getUserMenuDetails (String token) {
        return getUserDetails(token).getAuthorityMenuDTOS();
    }

}
