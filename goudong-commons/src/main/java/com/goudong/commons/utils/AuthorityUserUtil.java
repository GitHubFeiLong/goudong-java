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

    @Autowired
    private RedisOperationsUtil redisOperationsUtil;

    /**
     * 登录统一处理redis
     * @param token
     * @param authorityUserDTO
     */
    public void login (String token, AuthorityUserDTO authorityUserDTO) {
        // 添加token保存到redis中
        redisOperationsUtil.setStringValue(RedisKeyEnum.OAUTH2_TOKEN_INFO, token, authorityUserDTO.getId().toString());

        // 将用户信息保存到redis中
        // 将token进行md5加密作为redis key(16位16进制字符串)，然后保存用户详细信息
        String tokenMd5Key = JwtTokenUtil.generateRedisKey(token);
        // 存储redis || 追加时长
        redisOperationsUtil.setHashValue(RedisKeyEnum.OAUTH2_USER_INFO, BeanUtil.beanToMap(authorityUserDTO), tokenMd5Key);
    }

    /**
     * 退出统一处理redis
     * @param token
     */
    public void logout(String token) {
        // 获取登录用户
        AuthorityUserDTO authorityUserDTO = JwtTokenUtil.resolveToken(token);

        // 将token进行md5加密作为redis key(16位16进制字符串)
        String tokenMd5Key = JwtTokenUtil.generateRedisKey(token);

        // 清除用户在线token,清除用户能访问的菜单
        RedisKeyEnum[] deleteKeys = {RedisKeyEnum.OAUTH2_TOKEN_INFO, RedisKeyEnum.OAUTH2_USER_INFO};
        // 对应参数二维数组
        String[][] params = {
                {authorityUserDTO.getId().toString()},
                {tokenMd5Key}
        };
        // 删除redis中的数据
        redisOperationsUtil.deleteKeys(deleteKeys, params);
    }

    /**
     * 使用HttpServletRequest 获取用户信息
     * 原理就是使用请求头的Authorization值，使用md5生成一个16位的key，从redis获取对象
     * @param request
     * @return
     */
    public AuthorityUserDTO getUserDetails(HttpServletRequest request){
        // 获取token
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        return getUserDetails(token);

    }
    /**
     * 根据请求对象获取用户id
     * @param request
     * @return
     */
    public Long getUserId(HttpServletRequest request) {
        // 获取token
        String token = request.getHeader(JwtTokenUtil.TOKEN_HEADER);
        return getUserDetails(token).getId();
    }

    /**
     * 根据请求对象，获取用户角色
     * @param request
     * @return
     */
    public List<AuthorityRoleDTO> getUserRoleDetails (HttpServletRequest request) {
        return getUserDetails(request).getAuthorityRoleDTOS();
    }

    /**
     * 根据请求对象，获取用户权限
     * @param request
     * @return
     */
    public List<AuthorityMenuDTO> getUserMenuDetails (HttpServletRequest request) {
        return getUserDetails(request).getAuthorityMenuDTOS();
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
